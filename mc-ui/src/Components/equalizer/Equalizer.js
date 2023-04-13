import React, { useState } from 'react';
import './Equalizer.css';

function Equalizer() {
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
        var lBands = {
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
        console.log(JSON.stringify(lBands));
        fetch('/mc/eq', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(lBands)
        })
        .then(response => response.json())
        .catch((error) => {
            console.error('Error:', error);
        });
    };

    const renderBandSliders = () => {
        return bands.map((band, index) => {
            return (
                <ul className="eq">
                    <ul key={band.frequency} className="band-slider">
                        <ul className="frequency-label">{
                            `${band.frequency} Hz`
                        }</ul>
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
                        <ul className="gain-label">{`${band.gain} dB`}</ul>
                        <ul className="nav">
                            <li className="plusmoins" onClick={() => {
                                let g = band.gain === 12 ? 12 : band.gain + 1;
                                handleBandChange(index, g);
                            }}>+</li>
                        </ul>
                        <ul className="nav">
                            <li className="plusmoins" onClick={() => {
                                let g = band.gain === -12 ? -12 : band.gain - 1;
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
}

export default Equalizer;
