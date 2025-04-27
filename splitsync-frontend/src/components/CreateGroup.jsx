import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createGroup, joinGroup } from "../services/api";

export default function CreateGroup() {
  const [groupName, setGroupName] = useState("");
  const [totalMembers, setTotalMembers] = useState("");
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [upi, setUpi] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const groupRes = await createGroup({ name: groupName, totalMembers: parseInt(totalMembers) });
      const groupId = groupRes.data.id;
      alert(`Group Created! Share this Group ID: ${groupId}`);

      // Now join user only AFTER group created
      await joinGroup({ groupId, name: userName, email, upi });

      // After successful join, navigate
      navigate(`/dashboard/${groupId}`);
      setTimeout(() => {
        window.location.reload();
      }, 200);  // give slight delay to complete joining
    } catch (err) {
      console.error(err);
      alert("Failed to create or join group.");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-blue-50">
      <h2 className="text-3xl font-bold mb-6">Create Group</h2>
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow-md w-80 space-y-4">
        <input
          type="text"
          placeholder="Group Name"
          className="input-style"
          value={groupName}
          onChange={(e) => setGroupName(e.target.value)}
          required
        />
        <input
          type="number"
          placeholder="Total Members"
          className="input-style"
          value={totalMembers}
          onChange={(e) => setTotalMembers(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Your Name"
          className="input-style"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Your Email"
          className="input-style"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Your UPI ID"
          className="input-style"
          value={upi}
          onChange={(e) => setUpi(e.target.value)}
          required
        />
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-6 rounded-lg transition w-full"
        >
          Create & Join
        </button>
      </form>
    </div>
  );
}
