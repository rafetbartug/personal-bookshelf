import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Books from "./pages/Books";
import Navbar from "./components/Navbar";
import AddBook from "./pages/AddBook";
import AddAuthor from "./pages/AddAuthor";
import MyShelf from "./pages/MyShelf";
import ExternalSearch from "./pages/ExternalSearch";
import BookDetail from "./pages/BookDetail";
import Profile from "./pages/Profile";
import AdminDashboard from "./pages/AdminDashboard";
import AdminUsers from "./pages/AdminUsers";
import AdminReviews from "./pages/AdminReviews";
import AdminShelves from "./pages/AdminShelves";
import LandingPage from "./pages/LandingPage";
import { AuthProvider, AuthContext } from "./context/AuthContext";
import { useContext } from "react";

// Server Info Linki için URL mantığı
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
const SERVER_INFO_URL = API_BASE_URL.endsWith('/') ? `${API_BASE_URL}server-info` : `${API_BASE_URL}/server-info`;

// 1. STANDART KULLANICI KORUMASI
function PrivateRoute({ children }) {
    const { user } = useContext(AuthContext);
    return user ? children : <Navigate to="/login" />;
}

// 2. ADMIN KORUMASI
function AdminRoute({ children }) {
    const { user } = useContext(AuthContext);
    if (!user) return <Navigate to="/login" />;
    if (user.role !== 'ROLE_ADMIN') return <Navigate to="/books" />;
    return children;
}

function App() {
    return (
        <AuthProvider>
            <Router>
                <Navbar />

                {}
                <div className="d-flex flex-column" style={{ minHeight: "100vh", paddingTop: "80px" }}>

                    {/* Sayfa İçerikleri */}
                    <div className="flex-grow-1">
                        <Routes>
                            {/* --- HERKESE AÇIK ROTALAR --- */}
                            <Route path="/" element={<LandingPage />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />

                            {/* --- KULLANICI ROTALARI --- */}
                            <Route path="/books" element={<PrivateRoute><Books /></PrivateRoute>} />
                            <Route path="/books/:id" element={<PrivateRoute><BookDetail /></PrivateRoute>} />
                            <Route path="/myshelf" element={<PrivateRoute><MyShelf /></PrivateRoute>} />
                            <Route path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />

                            {/* --- ADMIN İŞLEMLERİ --- */}
                            <Route path="/admin" element={<Navigate to="/admin/dashboard" />} />
                            <Route path="/admin/dashboard" element={<AdminRoute><AdminDashboard /></AdminRoute>} />
                            <Route path="/admin/users" element={<AdminRoute><AdminUsers /></AdminRoute>} />
                            <Route path="/admin/reviews" element={<AdminRoute><AdminReviews /></AdminRoute>} />
                            <Route path="/admin/shelves" element={<AdminRoute><AdminShelves /></AdminRoute>} />
                            <Route path="/add-book" element={<AdminRoute><AddBook /></AdminRoute>} />
                            <Route path="/add-author" element={<AdminRoute><AddAuthor /></AdminRoute>} />
                            <Route path="/search-external" element={<AdminRoute><ExternalSearch /></AdminRoute>} />
                        </Routes>
                    </div>

                    {/* --- FOOTER (SERVER INFO) --- */}
                    <footer className="text-center py-3 mt-4 border-top bg-light">
                        <div className="container">
                            <small className="text-muted d-block mb-2">
                                &copy; 2025 Personal Bookshelf
                            </small>
                            <a
                                href={SERVER_INFO_URL}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="btn btn-sm btn-outline-secondary"
                                style={{ fontSize: '0.75rem', padding: '2px 8px' }}
                            >
                            </a>
                        </div>
                    </footer>

                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;