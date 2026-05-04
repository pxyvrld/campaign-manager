import { useState } from 'react';
import CampaignList from './components/CampaignList';
import CampaignForm from './components/CampaignForm';
import EmeraldBalance from './components/EmeraldBalance';
import './App.css';

function App() {
    const [showForm, setShowForm] = useState(false);
    const [editingCampaign, setEditingCampaign] = useState(null);
    const [refreshTrigger, setRefreshTrigger] = useState(0);

    const handleEdit = (campaign) => {
        setEditingCampaign(campaign);
        setShowForm(true);
    };

    const handleSave = () => {
        setShowForm(false);
        setEditingCampaign(null);
        setRefreshTrigger(prev => prev + 1);
    };

    const handleCancel = () => {
        setShowForm(false);
        setEditingCampaign(null);
    };

    const handleNewCampaign = () => {
        setEditingCampaign(null);
        setShowForm(true);
    };

    return (
        <div className="app">
            <div className="app-header">
                <h1>Campaign Manager</h1>
                <EmeraldBalance refreshTrigger={refreshTrigger} />
            </div>

            <button className="btn btn-primary" onClick={handleNewCampaign}>
                + New Campaign
            </button>

            {showForm && (
                <CampaignForm
                    editingCampaign={editingCampaign}
                    onSave={handleSave}
                    onCancel={handleCancel}
                />
            )}

            <CampaignList
                refreshTrigger={refreshTrigger}
                onEdit={handleEdit}
            />
        </div>
    );
}

export default App;