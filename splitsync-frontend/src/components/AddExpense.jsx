import { useState, useEffect } from "react";
import { addExpense, getGroup } from "../services/api";

export default function AddExpense({ groupId, onClose }) {
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [paidBy, setPaidBy] = useState("");
  const [splitAmong, setSplitAmong] = useState([]);
  const [members, setMembers] = useState([]);

  useEffect(() => {
    const fetchMembers = async () => {
      const res = await getGroup(groupId);
      setMembers(res.data.members);
    };
    fetchMembers();
  }, []); // ✅ useEffect here, not useState

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addExpense(groupId, {
        amount: parseFloat(amount),
        description,
        category,
        paidBy,
        splitAmong,
      });
      alert("Expense added successfully!");
      onClose(); // closes popup and triggers fetchGroup
    } catch (err) {
      console.error(err);
      alert("Failed to add expense.");
    }
  };

  const handleCheckboxChange = (name) => {
    setSplitAmong((prev) =>
      prev.includes(name) ? prev.filter((n) => n !== name) : [...prev, name]
    );
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg w-96 shadow-lg">
        <h2 className="text-2xl font-bold mb-4">Add Expense</h2>
        <form onSubmit={handleSubmit} className="space-y-3">
          <input
            type="number"
            placeholder="Amount"
            className="input-style"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Description"
            className="input-style"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
          <select
  className="input-style"
  value={category}
  onChange={(e) => setCategory(e.target.value)}
  required
>
  <option value="">Select Category</option>
  <option value="Food">Food</option>
  <option value="Travel">Travel</option>
  <option value="Accommodation">Accommodation</option>
  <option value="Shopping">Shopping</option>
  <option value="Miscellaneous">Miscellaneous</option>
</select>
          <select
            className="input-style"
            value={paidBy}
            onChange={(e) => setPaidBy(e.target.value)}
            required
          >
            <option value="">Paid By</option>
            {members.map((m) => (
              <option key={m.id} value={m.name}>
                {m.name}
              </option>
            ))}
          </select>

          <div className="border p-2 rounded">
            <p className="font-semibold mb-1">Split Among:</p>
            {members.map((m) => (
              <div key={m.id} className="flex items-center">
                <input
                  type="checkbox"
                  checked={splitAmong.includes(m.name)}
                  onChange={() => handleCheckboxChange(m.name)}
                />
                <span className="ml-2">{m.name}</span>
              </div>
            ))}
          </div>

          <div className="flex justify-between">
            <button
              type="button"
              className="bg-gray-400 hover:bg-gray-500 text-white py-1 px-4 rounded"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-blue-500 hover:bg-blue-600 text-white py-1 px-4 rounded"
            >
              Add
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
