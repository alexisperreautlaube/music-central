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
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingUnselected");
                document.getElementById("rating2").classList.add("ratingUnselected");
                document.getElementById("rating3").classList.add("ratingUnselected");
                document.getElementById("rating4").classList.add("ratingUnselected");
                document.getElementById("rating5").classList.add("ratingSelected");
            } else if(myJson.rating == 80) {
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingUnselected");
                document.getElementById("rating2").classList.add("ratingUnselected");
                document.getElementById("rating3").classList.add("ratingUnselected");
                document.getElementById("rating4").classList.add("ratingSelected");
                document.getElementById("rating5").classList.add("ratingUnselected");
            } else if (myJson.rating == 60) {
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingUnselected");
                document.getElementById("rating2").classList.add("ratingUnselected");
                document.getElementById("rating3").classList.add("ratingSelected");
                document.getElementById("rating4").classList.add("ratingUnselected");
                document.getElementById("rating5").classList.add("ratingUnselected");
            } else if (myJson.rating == 40) {
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingUnselected");
                document.getElementById("rating2").classList.add("ratingSelected");
                document.getElementById("rating3").classList.add("ratingUnselected");
                document.getElementById("rating4").classList.add("ratingUnselected");
                document.getElementById("rating5").classList.add("ratingUnselected");
            } else if(myJson.rating == 20) {
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingSelected");
                document.getElementById("rating2").classList.add("ratingUnselected");
                document.getElementById("rating3").classList.add("ratingUnselected");
                document.getElementById("rating4").classList.add("ratingUnselected");
                document.getElementById("rating5").classList.add("ratingUnselected");
            } else if (myJson.rating == 0) {
                document.getElementById("rating1").classList.remove("ratingSelected");
                document.getElementById("rating2").classList.remove("ratingSelected");
                document.getElementById("rating3").classList.remove("ratingSelected");
                document.getElementById("rating4").classList.remove("ratingSelected");
                document.getElementById("rating5").classList.remove("ratingSelected");
                document.getElementById("rating1").classList.add("ratingUnselected");
                document.getElementById("rating2").classList.add("ratingUnselected");
                document.getElementById("rating3").classList.add("ratingUnselected");
                document.getElementById("rating4").classList.add("ratingUnselected");
                document.getElementById("rating5").classList.add("ratingUnselected");
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

    function playPause() {
        console.log('createBestOf');
        fetch(properties.serverUrl + '/trigger/playPause', {
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
        <div className="RatingTab">
            <div className="Rating">
                <ul className="nav">
                    <li className="artist" id="artist">
                        artist
                    </li>
                    <li className="album" id="album">
                        album
                    </li>
                </ul>
            </div>
            <div className="Rating">
                <ul className="nav">
                    <li className="track" id="track">
                        track
                    </li>
                </ul>
            </div>
            <div className="Rating">
                <ul className="nav">
                    <li className="ratingSelected" id="rating1" onClick={() => {
                        rate(1);
                        getCurrentSong();
                    }}>
                        *
                    </li>
                    <li className="ratingUnselected" id="rating2" onClick={() => { rate(2); }}>
                        * *
                    </li>
                    <li className="ratingUnselected" id="rating3" onClick={() => { rate(3); }}>
                        * * *
                    </li>
                    <li className="ratingUnselected" id="rating4" onClick={() => { rate(4); }}>
                        * * * *
                    </li>
                    <li className="ratingUnselected" id="rating5" onClick={() => { rate(5); }}>
                        * * * * *
                    </li>
                </ul>
                <ul className="nav">
                    <li onClick={() => { playPause(); }}>
                        Play/Pause
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