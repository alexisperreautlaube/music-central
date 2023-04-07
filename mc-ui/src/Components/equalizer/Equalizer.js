import React, { useState } from 'react';
import './Equalizer.css';

function Equalizer() {
    const [bands, setBands] = useState([
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
    };

    const renderBandSliders = () => {
        return bands.map((band, index) => {
            return (
                <div key={band.frequency} className="band-slider">
                    <div className="frequency-label">{`${band.frequency} Hz`}</div>
                    <div className="slider-container">
                        <input
                            type="range"
                            min="-12"
                            max="12"
                            value={band.gain}
                            className="slider"
                            onChange={(e) => handleBandChange(index, parseFloat(e.target.value))}
                        />
                    </div>
                    <div className="gain-label">{`${band.gain} dB`}</div>
                </div>
            );
        });
    };

    return (
        <div className="equalizer-container">
            <h2>Equalizer</h2>
            <div className="band-container">{renderBandSliders()}</div>
        </div>
    );
}

export default Equalizer;
