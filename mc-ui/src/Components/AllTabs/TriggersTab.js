// TriggersTab.js

import React from "react";
import {properties} from "../../properties/properties";
const TriggersTab = () => {
    function doAll() {
        console.log('doAll');
        fetch(properties.serverUrl + '/trigger/doAll', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }
    function saveAllTrackByList() {
        console.log('saveAllTrackByList');
        fetch(properties.serverUrl + '/trigger/saveAllTrackByList', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }
    function refreshAppleAvailableTrack() {
        console.log('refreshAppleAvailableTrack');
        fetch(properties.serverUrl + '/trigger/refreshAppleAvailableTrack', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }
    function refreshWeight() {
        console.log('refreshWeight');
        fetch(properties.serverUrl + '/trigger/refreshWeight', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }
    function createTriageList() {
        console.log('createTriageList');
        fetch(properties.serverUrl + '/trigger/createTriageList', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }

    function createBestOf() {
        console.log('createBestOf');
        fetch(properties.serverUrl + '/trigger/createBestOf', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }

    function createAndAssignEq() {
        console.log('createAndAssignEq');
        fetch(properties.serverUrl + '/trigger/createAndAssignEq', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        })
    }

    return (
        <div className="TriggersTab">
            <p>Triggers</p>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        doAll();
                    }}>
                        Do All
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        saveAllTrackByList();
                    }}>
                        Save All Track By List
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        refreshAppleAvailableTrack();
                    }}>
                        Refresh Apple Available Tracks
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        refreshWeight();
                    }}>
                        Refresh Weight
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        createTriageList();
                    }}>
                        Create Triage List
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        createBestOf();
                    }}>
                        Create Best of
                    </li>
                </ul>
            </div>
            <div className="Trigger">
                <ul className="nav">
                    <li onClick={() => {
                        createAndAssignEq();
                    }}>
                        Create And Assign Eq
                    </li>
                </ul>
            </div>
        </div>
    );
};
export default TriggersTab;
