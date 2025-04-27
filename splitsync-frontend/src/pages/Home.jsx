import { useNavigate } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 to-purple-200">
      <h1 className="text-4xl font-bold mb-8 text-gray-800">Welcome to SplitSync</h1>
      <div className="flex space-x-6">
        <button
          className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-8 rounded-lg shadow-lg transition"
          onClick={() => navigate("/create")}
        >
          Create Group
        </button>
        <button
          className="bg-green-500 hover:bg-green-600 text-white font-semibold py-3 px-8 rounded-lg shadow-lg transition"
          onClick={() => navigate("/join")}
        >
          Join Group
        </button>
      </div>
    </div>
  );
}
