import { useEffect, useState } from 'react';
import { createCampaign, updateCampaign, getKeywords, getTowns } from '../api/campaignApi';

function CampaignForm({ editingCampaign, onSave, onCancel }) {
    const [name, setName] = useState(editingCampaign?.name || '');
    const [keywords, setKeywords] = useState(editingCampaign?.keywords || []);
    const [keywordInput, setKeywordInput] = useState('');
    const [bidAmount, setBidAmount] = useState(editingCampaign?.bidAmount || '');
    const [campaignFund, setCampaignFund] = useState(editingCampaign?.campaignFund || '');
    const [status, setStatus] = useState(editingCampaign?.status ?? true);
    const [town, setTown] = useState(editingCampaign?.town || '');
    const [radius, setRadius] = useState(editingCampaign?.radius || '');
    const [availableTowns, setAvailableTowns] = useState([]);
    const [availableKeywords, setAvailableKeywords] = useState([]);
    const [keywordSuggestions, setKeywordSuggestions] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        let cancelled = false;

        getTowns()
            .then(response => {
                if (!cancelled) setAvailableTowns(response.data);
            })
            .catch(() => {
                if (!cancelled) setError('Failed to fetch towns');
            });

        getKeywords()
            .then(response => {
                if (!cancelled) setAvailableKeywords(response.data);
            })
            .catch(() => {
                if (!cancelled) setError('Failed to fetch keywords');
            });

        return () => { cancelled = true; };
    }, []);

    const handleKeywordInput = (value) => {
        setKeywordInput(value);
        if (value.length > 0) {
            const filtered = availableKeywords.filter(k =>
                k.toLowerCase().includes(value.toLowerCase())
            );
            setKeywordSuggestions(filtered);
        } else {
            setKeywordSuggestions([]);
        }
    };

    const addKeyword = (keyword) => {
        if (!keywords.includes(keyword)) {
            setKeywords([...keywords, keyword]);
        }
        setKeywordInput('');
        setKeywordSuggestions([]);
    };

    const removeKeyword = (keyword) => {
        setKeywords(keywords.filter(k => k !== keyword));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        const payload = {
            name,
            keywords,
            bidAmount: parseFloat(bidAmount),
            campaignFund: parseFloat(campaignFund),
            status,
            town: town || null,
            radius: parseInt(radius),
        };

        try {
            if (editingCampaign) {
                await updateCampaign(editingCampaign.id, payload);
            } else {
                await createCampaign(payload);
            }
            onSave();
        } catch (err) {
            setError(err.response?.data?.message || 'Something went wrong');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>{editingCampaign ? 'Edit Campaign' : 'New Campaign'}</h2>

            {error && <div style={{ color: 'red' }}>{error}</div>}

            <div>
                <label>Campaign Name *</label>
                <input
                    type="text"
                    value={name}
                    onChange={e => setName(e.target.value)}
                    required
                />
            </div>

            <div>
                <label>Keywords *</label>
                <input
                    type="text"
                    value={keywordInput}
                    onChange={e => handleKeywordInput(e.target.value)}
                    placeholder="Type to search keywords..."
                />
                {keywordSuggestions.length > 0 && (
                    <ul>
                        {keywordSuggestions.map(k => (
                            <li key={k} onClick={() => addKeyword(k)} style={{ cursor: 'pointer' }}>
                                {k}
                            </li>
                        ))}
                    </ul>
                )}
                <div>
                    {keywords.map(k => (
                        <span key={k}>
                            {k} <button type="button" onClick={() => removeKeyword(k)}>x</button>
                        </span>
                    ))}
                </div>
            </div>

            <div>
                <label>Bid Amount (min 0.01) *</label>
                <input
                    type="number"
                    step="0.01"
                    min="0.01"
                    value={bidAmount}
                    onChange={e => setBidAmount(e.target.value)}
                    required
                />
            </div>

            <div>
                <label>Campaign Fund *</label>
                <input
                    type="number"
                    step="0.01"
                    min="0.01"
                    value={campaignFund}
                    onChange={e => setCampaignFund(e.target.value)}
                    required
                />
            </div>

            <div>
                <label>Status *</label>
                <select value={status} onChange={e => setStatus(e.target.value === 'true')}>
                    <option value="true">On</option>
                    <option value="false">Off</option>
                </select>
            </div>

            <div>
                <label>Town</label>
                <select value={town} onChange={e => setTown(e.target.value)}>
                    <option value="">-- Select town --</option>
                    {availableTowns.map(t => (
                        <option key={t} value={t}>{t}</option>
                    ))}
                </select>
            </div>

            <div>
                <label>Radius (km) *</label>
                <input
                    type="number"
                    min="1"
                    value={radius}
                    onChange={e => setRadius(e.target.value)}
                    required
                />
            </div>

            <button type="submit">
                {editingCampaign ? 'Update' : 'Create'}
            </button>
            {onCancel && (
                <button type="button" onClick={onCancel}>Cancel</button>
            )}
        </form>
    );
}

export default CampaignForm;