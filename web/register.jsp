<%-- 
    Document   : register_duy
    Created on : May 30, 2025, 6:18:28 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .form-container {
            background: white;
            width: 360px;
            padding: 40px 30px;
            border-radius: 15px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
        }

        h2 {
            margin-bottom: 10px;
            font-size: 24px;
            font-weight: bold;
            text-align: center;
        }

        p {
            text-align: center;
            color: #666;
            font-size: 14px;
        }

        input[type="email"],
        input[type="password"],
        input[type="text"],
        select {
            width: 100%;
            padding: 12px 10px;
            margin-top: 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
        }

        .form-footer {
            display: flex;
            justify-content: flex-end; /* Chỉnh lại nếu cần */
            align-items: center;
            margin-top: 10px;
            font-size: 14px;
        }

        .form-footer a {
            text-decoration: none;
            color: #007bff;
        }

        button {
            width: 100%;
            background-color: #007bff;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            margin-top: 20px;
            cursor: pointer;
        }

        .error {
            color: red;
            font-size: 13px;
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Register</h2>
    <p>Please sign up to continue</p>

    <form action="RegisterServlet_duy" method="post">
        <input type="email" name="email" placeholder="Email Address" required />
        <input type="password" name="password" placeholder="Password" required />
        <input type="text" name="fullName" placeholder="Full Name" required />
        <input type="text" name="phone" placeholder="Phone Number" required />

        <select name="role" required>
            <option value="">Select Role</option>
            <option value="TRAVELER">Traveler</option>
            <option value="GUIDE">Guide</option>
        </select>

        <div class="form-footer">
            <a href="login">Login</a>
        </div>

        <button type="submit">Sign Up</button>

        <div class="error">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
    </form>
</div>

</body>
</html>