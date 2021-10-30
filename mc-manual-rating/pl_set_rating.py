from properties.p import Property
from logging.handlers import TimedRotatingFileHandler
import sys, getopt
import logging
import os
import requests
import json
from kafka import KafkaProducer
from datetime import datetime
from bson import dumps
script_name = os.path.basename(__file__).replace('.py', '')

log = None
dry_run = True
secret = None

def main(argv):
    global log, dry_run, secret
    debug_log = False
    base_path = '../'
    rating = -1
    help = '{}.py -d <True/false>'.format(script_name)
    try:
        opts, args = getopt.getopt(argv,"lhd:b:r:")
    except getopt.GetoptError:
        print(help)
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print(help)
            sys.exit()
        elif opt in ("-d"):
            dry_run = arg == 'True'
        elif opt in ("-b"):
            base_path = arg
        elif opt in ("-r"):
            rating = int(arg)
        elif opt in ("-l"):
            debug_log = True

    prop = Property()
    config = prop.load_property_files('{}config/config.properties'.format(base_path))
    secret = prop.load_property_files('{}config/secret.properties'.format(base_path))
    if debug_log:
        config['log.level'] = 10
    logFormatter = logging.Formatter(config['log.format'])
    log = logging.getLogger(script_name)
    log.setLevel(int(config['log.level']))
    fileHandler = TimedRotatingFileHandler('{}logs/{}.log'.format(base_path, script_name), when="d", interval=1, backupCount=10, encoding='utf-8')
    fileHandler.setFormatter(logFormatter)
    log.addHandler(fileHandler)

    consoleHandler = logging.StreamHandler()
    consoleHandler.setFormatter(logFormatter)
    log.addHandler(consoleHandler)
    log.info('start - {}.py -d {}'.format(script_name, dry_run))
    producer = KafkaProducer(bootstrap_servers=secret['kafka.url'])

    volumio_url = secret['volumio.url'] + 'getState'
    response = requests.request('GET', volumio_url)
    #log.info('responce={}'.format(response.__dict__['_content']))
    if response.status_code not in (200, 201):
        codename = response.status_code
        errtext = response.text.replace('\n', ' ')
        log.warning('BadRequest2 (%s) %s %s; %s' % (response.status_code, codename, response.url, errtext))
    resp = response.json()
    #log.info('resp={}'.format(resp))
    data = {
        'trackId': resp['uri'],
        'rating': rating,
        'rateDate': to_json_datetime(datetime.now())
    }
    #log.info('data={}'.format(data))
    msg = {
        'event': 'VOLUMIO_MANUAL',
        'data': json.dumps(data)
    }
    #log.info('msg={}'.format(msg))
    encode = json.dumps(msg).encode('utf-8')
    producer.send('rating.message', encode)

    log.info('end - {}.py -d {}'.format(script_name, dry_run))

def to_json_datetime(date):
    if date is None:
        return None
    return_date = {
        'date': {
            'year': date.year,
            'month': date.month,
            'day': date.day
        },
        'time': {
            'hour': date.hour,
            'minute': date.minute,
            'second': date.second,
            'nano': 000000000
        }
    }
    return return_date



if __name__ == "__main__":
    main(sys.argv[1:])