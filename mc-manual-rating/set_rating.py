from properties.p import Property
from logging.handlers import TimedRotatingFileHandler
import sys, getopt
import logging
import os
import requests
import json

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

    mc_url = secret['mc.url'] + 'ratePlaying/' + rating
    log.info('mc_url={}'.format(mc_url))
    response = requests.request('POST', mc_url)
    if response.status_code not in (200, 201):
        codename = response.status_code
        errtext = response.text.replace('\n', ' ')
        log.warning('BadRequest2 (%s) %s %s; %s' % (response.status_code, codename, response.url, errtext))

    log.info('end - {}.py -d {}'.format(script_name, dry_run))

if __name__ == "__main__":
    main(sys.argv[1:])