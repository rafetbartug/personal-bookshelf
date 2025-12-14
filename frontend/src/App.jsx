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
import LandingPage from "./pages/LandingPage"; // <-- YENİ EKLENDİ
import { AuthProvider, AuthContext } from "./context/AuthContext";
import { useContext } from "react";

// 1. STANDART KULLANICI KORUMASI (Giriş yapmayan giremez)
function PrivateRoute({ children }) {
    const { user } = useContext(AuthContext);
    return user ? children : <Navigate to="/login" />;
}

// 2. ADMIN KORUMASI (Sadece Admin girebilir, User giremez)
function AdminRoute({ children }) {
    const { user } = useContext(AuthContext);

    // Giriş yapmamışsa Login'e at
    if (!user) return <Navigate to="/login" />;

    // Giriş yapmış ama Admin değilse Kitaplara at
    if (user.role !== 'ROLE_ADMIN') return <Navigate to="/books" />;

    // Adminsi geç
    return children;
}

function App() {
    return (
        <AuthProvider>
            <Router>
                <Navbar />
                <Routes>
                    {/* --- HERKESE AÇIK ROTALAR --- */}
                    {/* Ana Sayfa artık Landing Page (Tanıtım) */}
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />

                    {/* --- KULLANICI ROTALARI (Giriş Şart) --- */}
                    <Route path="/books" element={<PrivateRoute><Books /></PrivateRoute>} />
                    <Route path="/books/:id" element={<PrivateRoute><BookDetail /></PrivateRoute>} />
                    <Route path="/myshelf" element={<PrivateRoute><MyShelf /></PrivateRoute>} />
                    <Route path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />

                    {/* --- ADMIN İŞLEMLERİ (Admin Rolü Şart) --- */}

                    {/* /admin yazınca direkt dashboard'a yönlendir */}
                    <Route path="/admin" element={<Navigate to="/admin/dashboard" />} />

                    <Route path="/admin/dashboard" element={<AdminRoute><AdminDashboard /></AdminRoute>} />
                    <Route path="/admin/users" element={<AdminRoute><AdminUsers /></AdminRoute>} />
                    <Route path="/admin/reviews" element={<AdminRoute><AdminReviews /></AdminRoute>} />
                    <Route path="/admin/shelves" element={<AdminRoute><AdminShelves /></AdminRoute>} />

                    <Route path="/add-book" element={<AdminRoute><AddBook /></AdminRoute>} />
                    <Route path="/add-author" element={<AdminRoute><AddAuthor /></AdminRoute>} />
                    <Route path="/search-external" element={<AdminRoute><ExternalSearch /></AdminRoute>} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;