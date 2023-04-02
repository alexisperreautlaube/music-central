import './Tabs.css';

import React, { useState } from "react";
import RatingTab from "../AllTabs/RatingTab";
import SecondTab from "../AllTabs/SecondTab";

const Tabs = () => {
    const [activeTab, setActiveTab] = useState("ratingTab");
    //  Functions to handle Tab Switching
    const handlerating = () => {
        // update the state to tab1
        setActiveTab("ratingTab");
    };
    const handleTab2 = () => {
        // update the state to tab2
        setActiveTab("tab2");
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
                    className={activeTab === "tab2" ? "active" : ""}
                    onClick={handleTab2}
                >
                    Tab 2
                </li>
            </ul>

            <div className="outlet">
                {activeTab === "ratingTab" ? <RatingTab /> : <SecondTab />}
            </div>
        </div>
    );
};
export default Tabs;