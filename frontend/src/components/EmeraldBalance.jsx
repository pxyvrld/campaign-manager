import { useEffect, useState } from 'react';
import { getEmeraldBalance } from '../api/campaignApi';
import './EmeraldBalance.css';

function EmeraldBalance({ refreshTrigger }) {
    const [balance, setBalance] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        let cancelled = false;

        getEmeraldBalance()
            .then(response => {
                if (!cancelled) setBalance(response.data.balance);
            })
            .catch(() => {
                if (!cancelled) setError('Failed to fetch balance');
            });

        return () => { cancelled = true; };
    }, [refreshTrigger]);

    if (error) return <div>{error}</div>;
    if (balance === null) return <div>Loading...</div>;

    return (
        <div className="emerald-balance">
            <span className="emerald-balance__label">Emerald Balance:</span>
            <span className="emerald-balance__amount">{balance} zł</span>
        </div>
    );
}

export default EmeraldBalance;