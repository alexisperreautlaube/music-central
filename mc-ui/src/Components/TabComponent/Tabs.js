import './Tabs.css';

import React, { useState } from "react";
import RatingTab from "../AllTabs/RatingTab";
import TriggersTab from "../AllTabs/TriggersTab";

const Tabs = () => {
    const [activeTab, setActiveTab] = useState("ratingTab");
    //  Functions to handle Tab Switching
    const handlerating = () => {
        // update the state to tab1
        setActiveTab("ratingTab");
    };
    const handleTab2 = () => {
        // update the state to tab2
        setActiveTab("triggersTab");
    };
    return (
        <div className="Tabs">
            <ul className="nav">
                <li
                    className={activeTab === "ratingTab" ? "active" : ""}
                    onClick={handlerating}
                >
                    Rating
                </li>
                <li
                    className={activeTab === "triggersTab" ? "active" : ""}
                    onClick={handleTab2}
                >
                    Triggers
                </li>
            </ul>

            <div className="outlet">
                {activeTab === "ratingTab" ? <RatingTab /> : <TriggersTab />}
            </div>
        </div>
    );
};
export default Tabs;