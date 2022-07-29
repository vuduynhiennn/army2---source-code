<?php
  $title = "Quên Chữ Ký - MobiArmy II";
  include './include/head.php';
  include './include/connect.php';
  if (isset($_SESSION['user_id'])) {
    header('Location: /profile');
    exit;
  }
  $error = '';
  $isError = false;
  if (isset($_POST['confirm'])) {
    $username              = $_POST['username'];
    $email                 = $_POST['email'];
    if ($username == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không được bỏ trống!</strong></span><br>';
    }
    if ($email == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Mật khẩu không được bỏ trống!</strong></span><br>';
    }
    if ($username != null && !preg_match("/^[a-zA-Z0-9_-]{5,16}$/", $username)) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không hợp lệ!</strong></span><br>';
    }
    if ($email != null && !preg_match('/^.+@.+$/', $email)) {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Định dạng email không hợp lệ!</strong></span><br>';
    }
    if (!$isError) {
      include './include/sendmail.php';
      $sql = "SELECT `password` FROM `user` WHERE `email` = '".$game->real_escape_string($email)."' AND `user` = '".$game->real_escape_string($username)."';";
      $r = $game->query($sql);
      if ($r->num_rows > 0) {
        $r = $r->fetch_assoc();
        $contents = "Mật khẩu của bạn là: ".$r['password'];
        $req = sendMail($email, 'Quên mật khẩu', $contents);
        if ($req['status']) {
          $success .= '<span class="invalid-feedback" role="alert" style="color:green"><strong>Mật khẩu đã được gửi vào email.</strong></span><br>';
        } else {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Lỗi! '.$req['message'].'</strong></span><br>';
        }
      } else {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản hoặc Email không chính xác!</strong></span><br>';
      }
    }
  }
?>
                
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Quên Mật Khẩu</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="POST">
        <span style="margin-left:-85px; font-family: AVO, Arial !important;">Tên tài khoản</span><br>
        <input name="username" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-130px;font-family: AVO, Arial !important;"> Email </span><br>
        <input name="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <?php echo $error; echo $success; ?>
        <button name="confirm">Xác nhận</button><br><br>
        <p>Bạn đã có tài khoản? <a href="/login" style="color:red">Đăng nhập</a> ngay</p>
      <form>
    </div>
  </div>
</div>
<?php
  include './include/end.php';
?>