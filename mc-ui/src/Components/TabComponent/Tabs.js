import React, { useState } from "react";
import DistanceTab from "../AllTabs/DistanceTab";
import SecondTab from "../AllTabs/SecondTab";

const Tabs = () => {
    const [activeTab, setActiveTab] = useState("distanceTab");
    //  Functions to handle Tab Switching
    const handleDistance = () => {
        // update the state to tab1
        setActiveTab("distanceTab");
    };
    const handleTab2 = () => {
        // update the state to tab2
        setActiveTab("tab2");
    };
    return (
        <div className="Tabs">
            <ul className="nav">
                <li
                    className={activeTab === "distanceTab" ? "active" : ""}
                    onClick={handleDistance}
                >
                    Distance
                </li>
                <li
                    className={activeTab === "tab2" ? "active" : ""}
                    onClick={handleTab2}
                >
                    Tab 2
                </li>
            </ul>

            <div className="outlet">
                {activeTab === "distanceTab" ? <DistanceTab /> : <SecondTab />}
            </div>
        </div>
    );
};
export default Tabs;