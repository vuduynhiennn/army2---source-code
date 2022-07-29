
<?php 
  $title = "Trang chủ - MobiArmy II"; 
  include './include/head.php';
?>
                
    <div class="bg_top_22">
        <div class="w3-content w3-display-container">
            <img class="mySlides" src="/public/images/boss1.png" alt="MobiArmy II" style="width:100%">
            <img class="mySlides" src="/public/images/boss2.png" alt="MobiArmy II" style="width:100%">
            <img class="mySlides" src="/public/images/boss3.png" alt="MobiArmy II" style="width:100%">
            <img class="mySlides" src="/public/images/boss4.png" alt="MobiArmy II" style="width:100%">
            <img class="mySlides" src="/public/images/boss5.png" alt="MobiArmy II" style="width:100%">
            <img class="mySlides" src="/public/images/boss6.png" alt="MobiArmy II" style="width:100%">
        </div>
        <script>
            var myIndex = 0;
            carousel();

            function carousel() {
                var i;
                var x = document.getElementsByClassName("mySlides");
                for (i = 0; i < x.length; i++) {
                    x[i].style.display = "none";
                }
                myIndex++;
                if (myIndex > x.length) {
                    myIndex = 1
                }
                x[myIndex - 1].style.display = "block";
                setTimeout(carousel, 1500);
            }
        </script>
    </div>

    
<? include './include/download.php'; ?>



<div class="bg-content">
    <div>
        <div class="title">
            <h4>Giới Thiệu</h4>
        </div>
        <div class="content" style="text-indent: 3%">
                <p>MobiArmy2 là game mobile nhập vai chơi theo lượt với phong cách đồ họa sinh động, lối chơi đa dạng và đầy hấp dẫn.</p>
                <p>Trong game người chơi phải tính toán góc bắn và lực bắn đồng thời phải kết hợp với sức gió để đường đi của viên đạn trúng mục tiêu. Với lối chơi đa dạng người chơi có thể chiến đấu với nhau, hoặc lập thành 1 nhóm để tham gia khiêu chiến với Boss.</p>
                <p>Phiên bản Mobi Army 2 với nhiều lớp nhân vật hấp dẫn <font color="red">Rocketer</font>, <font color="red">King Kong</font>, <font color="red">Granos</font> , <font color="red">Tarzan</font> , <font color="red">Chicky</font> , <font color="red">Apache</font> , <font color="red">Gunner</font> , <font color="red">Electrician</font> , <font color="red">Miss 6</font> với tuyệt chiêu riêng biệt đặc sắc mang đậm dấu ấn cá nhân và xuất xứ của nhân vật, bên cạnh đó với những vật phẩm mới độc đáo như: Đạn vòi rồng, Chuột gắn bom, Tên lửa, Đạn xuyên đất, Sao băng, Mưa đạn, Khoan đất…Cuộc tranh tài của các bạn sẽ càng hấp dẫn hơn, khốc liệt hơn và đầy bất ngờ hơn. Mobi Army 2 còn hấp dẫn hơn với những vùng chiến đấu mới như: vùng băng tuyết, vùng căn cứ thép, những hoang mạc và những đồng cỏ, rừng chết.</p>
                <p>Có vô số các bị với chỉ số khác nhau. Mỗi lớp nhân vật đều có trang bị riêng của mình</p>
                <p>Thật hấp dẫn phải không nào!! Nào cùng tham gia chiến đấu để tranh tài cao thấp nào!!!</p>
        </div>

    </div>
</div>
<div class="bg-content">
    <div>
        <div class="title">
            <h4>Hướng Dẫn Tân Thủ</h4></div>
        <div class="content">
            <p><strong>1. Đăng kí tài khoản</strong></p>

            <ul>
            </ul>

            <p>- MobiArmy II không sử dụng chung tài khoản với bất kì game nào!
                <br /> - Bạn có thể đăng kí miễn phí ngay <a href="/register" style="color: black"><b>tại đây</b></a>
                <br /> - Khi đăng ký, bạn nên sử dụng đúng số điện thoại hoặc email thật của mình. Nếu sử dụng thông tin sai, người có số điện thoại hoặc email thật sẽ có thể lấy mật khẩu của bạn. 
                <br /> - Số điện thoại và email của bạn sẽ không hiện ra cho người khác thấy. Admin không bao giờ hỏi mật khẩu của bạn.</p>

            <p><strong>2. Hướng dẫn điều khiển</strong></p>

            <ul>
            </ul>

            <p>- Đối với máy bàn phím : Dùng các phím mũi tên số 4-6 để di chuyển, số 2-8 để chỉnh góc bắn. Giữ phím chọn giữa để canh lực bắn
                Đối với máy cảm ứng : Dùng tay chạm vào mũi tên để di chuyển. Chạm vào nút chỉnh góc sau lưng nhân vật để canh góc bắn, chạm và giữ nút bắn góc phải màn hình để canh lực bắn.</p>

            <p><strong>3. Một số th&ocirc;ng tin căn bản</strong></p>

            <ul>
            </ul>

            <p>- L&agrave;m nhiệm vụ sẽ gi&uacute;p bạn t&iacute;ch lũy kinh nghiệm thăng cấp nhanh hơn l&agrave;.
              <br /> - Mỗi trang bị đều có chỉ số sức mạnh riêng.</p>
        </div>

    </div>
</div>
<div class="bg-content">
    <div>
        <div class="title">
            <h4>Bạn nên tải phiên bản nào?</h4></div>
        <div class="content">
            <p>- Nếu bạn sử dụng điện thoại Nokia cũ (N series, E series, S40, S60...) hãy tải <a href="/download/java" style="color: black"><b>phiên bản</b></a> bản giành cho Java.</p>

            <p>- Nếu bạn đang sử dụng điện thoại chạy Android, bạn có thể tải và sử dụng <a href="/download/android" style="color: black"><b>phiên bản</b></a> giành cho Android.</p>

            <p>- Nếu bạn sử dụng máy tính đang chạy Windows, bạn hãy tải <b><a href="/download/pc" style="color: black">giả lập giành cho máy tính.</a></b></p>

            <p>- Rất tiếc rằng hiện tại game chưa hỗ trợ IOS.</p>
        </div>
    </div>
</div>
	</div>
                            <br>
                        </div>
                        <br>

                    </div>
                </div>
            </div>
            <div class="bg_tree"></div>
            
<?php include './include/end.php'; ?>