import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import CreateGroup from "./components/CreateGroup";
import JoinGroup from "./components/JoinGroup";
import Dashboard from "./components/Dashboard";


function App() {
  return (
    <div className="p-4">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/create" element={<CreateGroup />} />
        <Route path="/join" element={<JoinGroup />} />
        <Route path="/dashboard/:groupId" element={<Dashboard />} />
      </Routes>
    </div>
  );
}

export default App;
