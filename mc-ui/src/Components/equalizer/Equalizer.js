import React, { useState, forwardRef, useImperativeHandle } from 'react';
import './Equalizer.css';

const Equalizer = forwardRef((props, ref) => {
    useImperativeHandle(ref, () => ({
        refreshEq(myJson) {
            handleBandChange(1, parseFloat(myJson.band1))
            handleBandChange(2, parseFloat(myJson.band2))
            handleBandChange(3, parseFloat(myJson.band3))
            handleBandChange(4, parseFloat(myJson.band4))
            handleBandChange(5, parseFloat(myJson.band5))
            handleBandChange(6, parseFloat(myJson.band6))
            handleBandChange(7, parseFloat(myJson.band7))
            handleBandChange(8, parseFloat(myJson.band8))
            handleBandChange(9, parseFloat(myJson.band9))
            handleBandChange(10, parseFloat(myJson.band10))
        },
    }));


    const [bands, setBands] = useState([
        { frequency: "V", gain: 0 },
        { frequency: 32, gain: 0 },
        { frequency: 64, gain: 0 },
        { frequency: 125, gain: 0 },
        { frequency: 250, gain: 0 },
        { frequency: 500, gain: 0 },
        { frequency: 1000, gain: 0 },
        { frequency: 2000, gain: 0 },
        { frequency: 4000, gain: 0 },
        { frequency: 8000, gain: 0 },
        { frequency: 16000, gain: 0 }
    ]);



    const handleBandChange = (index, gain) => {
        const newBands = [...bands];
        newBands[index].gain = gain;
        setBands(newBands);
        return  {
          'preamp': bands[0].gain,
          'f32' : bands[1].gain,
          'f64' : bands[2].gain,
          'f125' : bands[3].gain,
          'f250' : bands[4].gain,
          'f500' : bands[5].gain,
          'f1000' : bands[6].gain,
          'f2000' : bands[7].gain,
          'f4000' : bands[8].gain,
          'f8000' : bands[9].gain,
          'f16000' : bands[10].gain,
        };
    };

    const handleBandChangeAndPost = (index, gain) => {
        handleBandChange(index, gain)
        //console.log(JSON.stringify(lBands));
        // fetch('/mc/eq', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify(lBands)
        // })
        // .then(response => response.json())
        // .catch((error) => {
        //     console.error('Error:', error);
        // });
    };

    const renderBandSliders = () => {
        return bands.map((band, index) => {
            return (
                <ul className="eq" name={band.frequency}>
                    <ul key={band.frequency} className="band-slider">
                        <ul className="gain-label">{`${band.gain}`}</ul>
                        <ul className="nav">
                            <li className="plusmoins" onClick={() => {
                                let g = band.gain === 12 ? 12 : band.gain + 0.5;
                                handleBandChange(index, g);
                            }}>+</li>
                        </ul>
                        <ul className="slider-container">
                            <input
                                type="range"
                                min="-12"
                                max="12"
                                value={band.gain}
                                className="slider"
                                onChange={(e) => handleBandChange(index, parseFloat(e.target.value))}
                            />
                        </ul>

                        <ul className="nav">
                            <li className="plusmoins" onClick={() => {
                                let g = band.gain === -12 ? -12 : band.gain - 0.5;
                                handleBandChange(index, g);
                            }}>-</li>
                        </ul>
                    </ul>
                </ul>
            );
        });
    };

    return (
        <ul>
            <div className="equalizer-container">
                <div className="band-container">{renderBandSliders()}</div>
            </div>
        </ul>
    );

});

export default Equalizer;
