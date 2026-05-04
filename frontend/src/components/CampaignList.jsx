import { useEffect, useState } from 'react';
import { getAllCampaigns, deleteCampaign } from '../api/campaignApi';

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
        <div>
            <h2>Campaigns</h2>
            {campaigns.length === 0 ? (
                <p>No campaigns yet. Create one!</p>
            ) : (
                <table>
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
                                <td>{campaign.status ? 'On' : 'Off'}</td>
                                <td>{campaign.town || '-'}</td>
                                <td>{campaign.radius} km</td>
                                <td>
                                    <button onClick={() => onEdit(campaign)}>Edit</button>
                                    <button onClick={() => handleDelete(campaign.id)}>Delete</button>
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