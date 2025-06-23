<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đánh giá Hướng dẫn viên</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            color: #333;
        }
        
        .container {
            max-width: 800px;
            margin: 50px auto;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        
        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }
        
        .rating-form {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
        }
        
        .star-rating {
            display: flex;
            flex-direction: row-reverse;
            justify-content: flex-end;
        }
        
        .star-rating input {
            display: none;
        }
        
        .star-rating label {
            cursor: pointer;
            font-size: 30px;
            color: #ddd;
            margin-right: 5px;
        }
        
        .star-rating label:before {
            content: '\f005';
            font-family: 'Font Awesome 5 Free';
        }
        
        .star-rating input:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: #f8ce0b;
        }
        
        .star-rating input:checked + label:hover,
        .star-rating input:checked ~ label:hover,
        .star-rating label:hover ~ input:checked ~ label,
        .star-rating input:checked ~ label:hover ~ label {
            color: #f8ce0b;
        }
        
        textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            resize: vertical;
            min-height: 120px;
            font-family: inherit;
        }
        
        button {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            transition: background-color 0.3s;
        }
        
        button:hover {
            background-color: #2980b9;
        }
        
        .error-message {
            color: #e74c3c;
            margin-top: 20px;
            text-align: center;
            font-weight: 500;
        }
        
        .rating-description {
            display: flex;
            justify-content: space-between;
            margin-top: 5px;
            color: #7f8c8d;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Đánh giá Hướng dẫn viên</h1>
        
        <% if(request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>
        
        <form class="rating-form" action="rating" method="post">
            <input type="hidden" name="bookingId" value="${bookingId}">
            <input type="hidden" name="guideId" value="${guideId}">
            
            <div class="form-group">
                <label>Đánh giá của bạn:</label>
                <div class="star-rating">
                    <input type="radio" id="star5" name="rating" value="5" required />
                    <label for="star5" title="5 sao"></label>
                    <input type="radio" id="star4" name="rating" value="4" />
                    <label for="star4" title="4 sao"></label>
                    <input type="radio" id="star3" name="rating" value="3" />
                    <label for="star3" title="3 sao"></label>
                    <input type="radio" id="star2" name="rating" value="2" />
                    <label for="star2" title="2 sao"></label>
                    <input type="radio" id="star1" name="rating" value="1" />
                    <label for="star1" title="1 sao"></label>
                </div>
                <div class="rating-description">
                    <span>Rất tệ</span>
                    <span>Tuyệt vời</span>
                </div>
            </div>
            
            <div class="form-group">
                <label for="comment">Nhận xét của bạn:</label>
                <textarea id="comment" name="comment" placeholder="Chia sẻ trải nghiệm của bạn về hướng dẫn viên..."></textarea>
            </div>
            
            <button type="submit">Gửi đánh giá</button>
        </form>
    </div>
    
    <script>
        // Hiển thị mô tả đánh giá khi người dùng chọn số sao
        document.querySelectorAll('input[name="rating"]').forEach(input => {
            input.addEventListener('change', function() {
                const value = this.value;
                let description = '';
                
                switch(value) {
                    case '1':
                        description = 'Rất tệ';
                        break;
                    case '2':
                        description = 'Không hài lòng';
                        break;
                    case '3':
                        description = 'Bình thường';
                        break;
                    case '4':
                        description = 'Hài lòng';
                        break;
                    case '5':
                        description = 'Tuyệt vời';
                        break;
                }
                
                document.getElementById('rating-description').textContent = description;
            });
        });
    </script>
</body>
</html>
