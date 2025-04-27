import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { joinGroup, getGroup } from "../services/api";

export default function JoinGroup() {
  const [groupId, setGroupId] = useState("");
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [upi, setUpi] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const groupRes = await getGroup(groupId);
      if (groupRes.data.members.length >= groupRes.data.totalMembers) {
        alert("Group is full! Cannot join.");
        return;
      }
      await joinGroup({ groupId, name: userName, email, upi });
      alert("Joined group successfully!");
      navigate(`/dashboard/${groupId}`);
      window.location.reload();  // Reload to show members correctly
    } catch (err) {
      console.error(err);
      alert("Invalid Group ID or failed to join.");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-green-50">
      <h2 className="text-3xl font-bold mb-6">Join Group</h2>
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow-md w-80 space-y-4">
        <input
          type="text"
          placeholder="Group ID"
          className="input-style"
          value={groupId}
          onChange={(e) => setGroupId(e.target.value)}
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
          className="bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-6 rounded-lg transition w-full"
        >
          Join
        </button>
      </form>
    </div>
  );
}
