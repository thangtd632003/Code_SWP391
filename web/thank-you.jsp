<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cảm ơn bạn</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        
        .container {
            max-width: 600px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            padding: 40px;
            text-align: center;
        }
        
        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }
        
        p {
            font-size: 18px;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .success-icon {
            font-size: 80px;
            color: #2ecc71;
            margin-bottom: 20px;
        }
        
        .home-button {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.3s;
        }
        
        .home-button:hover {
            background-color: #2980b9;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div class="container">
        <div class="success-icon">
            <i class="fas fa-check-circle"></i>
        </div>
        <h1>Cảm ơn bạn!</h1>
        <p>${successMessage != null ? successMessage : 'Cảm ơn bạn đã gửi đánh giá. Phản hồi của bạn rất quan trọng đối với chúng tôi!'}</p>
        <a href="dashboard.jsp" class="home-button">Quay về trang chủ</a>
    </div>
</body>
</html>
