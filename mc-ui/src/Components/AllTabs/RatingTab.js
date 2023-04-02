// RatingTab.js

import './RatingTab.css';
import React from "react";
import { properties } from '../../properties/properties.js';

const RatingTab = () => {

    function startRefresh() {
        getCurrentSong()
        setInterval(getCurrentSong, 5000);
    }

    function getCurrentSong() {
        console.log('getCurrentSong');
        fetch(properties.serverUrl+'/client/getCurrentSong', {
            method: 'GET',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }).then(function(response){
            console.log(response)
            return response.json();
        }).then(function(myJson) {
            console.log(myJson);
            document.getElementById('artist').innerText = myJson.artist;
            document.getElementById('album').innerText = myJson.album;
            document.getElementById('track').innerText = myJson.name;
            if (myJson.rating == 100) {
                document.getElementById('rating').innerText = '* * * * *';
            } else if(myJson.rating == 80) {
                document.getElementById('rating').innerText = '* * * * ';
            } else if (myJson.rating == 60) {
                document.getElementById('rating').innerText = '* * *';
            } else if (myJson.rating == 40) {
                document.getElementById('rating').innerText = '* *';
            } else if(myJson.rating == 20) {
                document.getElementById('rating').innerText = '*';
            } else if (myJson.rating == 0) {
                document.getElementById('rating').innerText = '';
            }
        });
    }
    function rate(i) {
        console.log('rate:' + i);
        fetch(properties.serverUrl+'/rate/' + i, {
            method: 'POST',
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        });
    }


    return (
        <div className="RatingTab">
            <p>Rating Tab!!</p>
            <div className="Rating">
                <ul className="nav">
                    <li className="rating" id="rating">
                        rating
                    </li>
                    <li className="artist" id="artist">
                        artist
                    </li>
                    <li className="album" id="album">
                        album
                    </li>
                    <li className="track" id="track">
                        track
                    </li>
                </ul>
            </div>
            <div className="Rating">
                <ul className="nav">
                    <li onClick={() => {
                        rate(1);
                        getCurrentSong();
                    }}>
                        *
                    </li>
                    <li onClick={() => { rate(2); }}>
                        * *
                    </li>
                </ul>
                <ul className="nav">
                    <li onClick={() => { rate(3); }}>
                        * * *
                    </li>
                    <li onClick={() => { rate(4); }}>
                        * * * *
                    </li>
                </ul>
                <ul className="nav">
                    <li onClick={() => { rate(5); }}>
                        * * * * *
                    </li>
                    <li onClick={() => { startRefresh(); }}>
                       Start Refresh
                    </li>
                </ul>
            </div>
        </div>
    );
};
export default RatingTab;