#!/usr/bin/python
from pymongo import MongoClient
import logging
import sys, getopt
from properties.p import Property
import os
import time

import tidalapi


try:
    from xml.etree import cElementTree as ElementTree
except ImportError:
    from xml.etree import ElementTree

script_name = os.path.basename(__file__).replace('.py', '')
script_config = None

log = None

db = None

def main(argv):
    global log, script_config, db


    prop = Property()
    config = prop.load_property_files('config/config.properties')
    secret = prop.load_property_files('config/secret.properties')

    logFormatter = logging.Formatter(config['log.format'])
    log = logging.getLogger(script_name)
    log.setLevel(int(config['log.level']))

    consoleHandler = logging.StreamHandler()
    consoleHandler.setFormatter(logFormatter)
    log.addHandler(consoleHandler)
    log.info('start - {}.py'.format(script_name))

    client = MongoClient(secret['mongo.url'], int(secret['mongo.port']))
    db = client.local

    volumio_medias = db.volumioMedia


    session = tidalapi.Session()
    session.login_oauth_simple()
    log.info(session.check_login())
    i = 0
    for volumio_media in volumio_medias.find({'$and': [{'trackType':'tidal'}, {'albumReleaseDate': {'$exists': False}}]}):
        #log.info('media={}'.format(volumio_media))
        start = volumio_media['_id'].rfind('/') + 1
        id = volumio_media['_id'][start:]
        #log.info('original id={}, id={}'.format(volumio_media['_id'], id))

        try:
            tidalTrack = session.track()._get(id).tidal_release_date
            log.info('{};{}'.format(volumio_media['_id'], tidalTrack))
        except Exception:
            time.sleep(1)
        time.sleep(0.5)
        i = i + 1


    log.info('end - {}.py'.format(script_name))




if __name__ == "__main__":
    main(sys.argv[1:])
