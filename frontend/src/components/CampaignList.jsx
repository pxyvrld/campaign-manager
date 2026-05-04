import { useEffect, useState } from 'react';
import { getAllCampaigns, deleteCampaign } from '../api/campaignApi';
import './CampaignList.css';

function CampaignList({ refreshTrigger, onEdit }) {
    const [campaigns, setCampaigns] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        let cancelled = false;

        getAllCampaigns()
            .then(response => {
                if (!cancelled) setCampaigns(response.data);
            })
            .catch(() => {
                if (!cancelled) setError('Failed to fetch campaigns');
            });

        return () => { cancelled = true; };
    }, [refreshTrigger]);

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this campaign?')) return;

        try {
            await deleteCampaign(id);
            setCampaigns(campaigns.filter(c => c.id !== id));
        } catch {
            setError('Failed to delete campaign');
        }
    };

    if (error) return <div>{error}</div>;

    return (
        <div className="campaign-list">
            <h2>Campaigns</h2>
            {campaigns.length === 0 ? (
                <p className="campaign-list__empty">No campaigns yet. Create one!</p>
            ) : (
                <table className="campaign-table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Keywords</th>
                            <th>Bid Amount</th>
                            <th>Campaign Fund</th>
                            <th>Status</th>
                            <th>Town</th>
                            <th>Radius</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {campaigns.map(campaign => (
                            <tr key={campaign.id}>
                                <td>{campaign.name}</td>
                                <td>{campaign.keywords.join(', ')}</td>
                                <td>{campaign.bidAmount} zł</td>
                                <td>{campaign.campaignFund} zł</td>
                                <td>
                                    <span className={`status-badge status-badge--${campaign.status ? 'on' : 'off'}`}>
                                        {campaign.status ? 'On' : 'Off'}
                                    </span>
                                </td>
                                <td>{campaign.town || '-'}</td>
                                <td>{campaign.radius} km</td>
                                <td>
                                    <div className="action-buttons">
                                        <button className="btn btn-primary" onClick={() => onEdit(campaign)}>Edit</button>
                                        <button className="btn btn-danger" onClick={() => handleDelete(campaign.id)}>Delete</button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default CampaignList;