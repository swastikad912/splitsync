import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getGroup } from "../services/api";
import AddExpense from "./AddExpense";
import Settlement from "./Settlement";

export default function Dashboard() {
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [showAddExpense, setShowAddExpense] = useState(false);
  const [showSettlement, setShowSettlement] = useState(false);
  const [settlementCount, setSettlementCount] = useState(0);  // New state to track settlement button clicks

  const fetchGroup = async () => {
    try {
      const res = await getGroup(groupId);
      setGroup(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchGroup();
  }, []);

  if (!group) return <div className="text-center mt-20 text-lg">Loading group details...</div>;

  const isGroupActive = group.members && group.members.length >= group.totalMembers;

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <h2 className="text-3xl font-bold mb-2 text-center">{group.name}</h2>
      <p className="text-center mb-4 text-gray-600">Group ID: {group.id}</p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div className="bg-white p-4 rounded-lg shadow">
          <h3 className="text-xl font-semibold mb-2">Members</h3>
          <ul className="list-disc list-inside">
            {group.members && group.members.length > 0 ? (
              group.members.map((member) => (
                <li key={member.id}>
                  {member.name} ({member.email})
                </li>
              ))
            ) : (
              <p>No members yet.</p>
            )}
          </ul>
        </div>

        <div className="bg-white p-4 rounded-lg shadow">
          <h3 className="text-xl font-semibold mb-2">Expenses</h3>
          {group.expenses && group.expenses.length > 0 ? (
            <ul className="space-y-2">
              {group.expenses.map((expense) => (
                <li key={expense.id} className="border p-2 rounded">
                  <strong>{expense.description}</strong> - ₹{expense.amount} (Paid by {expense.paidBy})
                </li>
              ))}
            </ul>
          ) : (
            <p>No expenses added yet.</p>
          )}
        </div>
      </div>

      {isGroupActive ? (
        <div className="flex justify-center space-x-4 mt-6">
          <button
            className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-6 rounded-lg"
            onClick={() => setShowAddExpense(true)}
          >
            Add Expense
          </button>
          <button
            className="bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-6 rounded-lg"
            onClick={() => {
              fetchGroup();
              setShowSettlement(true);
              setSettlementCount((prev) => prev + 1);  // Increment settlement count
            }}
          >
            Settle Up
          </button>
        </div>
      ) : (
        <p className="text-center text-red-600 font-semibold mt-6">Waiting for all members to join...</p>
      )}

      {showAddExpense && <AddExpense groupId={groupId} onClose={() => { setShowAddExpense(false); fetchGroup(); }} />}
      
      {/* Pass the settlementCount as the key to refresh the component on each button click */}
      {showSettlement && <Settlement key={settlementCount} groupId={groupId} />}
    </div>
  );
}
