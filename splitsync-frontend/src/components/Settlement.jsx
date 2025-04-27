import { useState, useEffect } from "react";
import { getSettlements } from "../services/api";

export default function Settlement({ groupId }) {
  const [settlements, setSettlements] = useState({});

  useEffect(() => {
    const fetchSettlements = async () => {
      const res = await getSettlements(groupId);
      setSettlements(res.data);
    };
    fetchSettlements();
  }, []);

  return (
    <div className="mt-8 bg-white p-6 rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4">Settlements</h2>
      {Object.keys(settlements).length === 0 ? (
        <p>No settlements found.</p>
      ) : (
        Object.entries(settlements).map(([debtor, creditors]) =>
          Object.entries(creditors).map(([creditor, amount]) => (
            <div key={`${debtor}-${creditor}`} className="border p-2 rounded mb-2">
              <strong>{debtor}</strong> owes <strong>{creditor}</strong> ₹{amount.toFixed(2)}
            </div>
          ))
        )
      )}
    </div>
  );
}
