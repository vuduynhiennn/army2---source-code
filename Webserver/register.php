<?php
$title = "Đăng Ký - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (isset($_SESSION['user_id'])) {
  header('Location: /profile');
  exit;
}
$error   = '';
$success = '';
$isError = false;
if (isset($_POST['register'])) {
    $username              = $_POST['username'];
    $password              = $_POST['password'];
    $password_confirmation = $_POST['password_confirmation'];
    $email                 = $_POST['email'];
    if ($username == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống tài khoản!</strong></span><br>';
    }
    if ($email == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống email!</strong></span><br>';
    }
    if ($password == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống mật khẩu!</strong></span><br>';
    }
    if ($password_confirmation == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống mật khẩu xác nhận!</strong></span><br>';
    }
    if ($password != null && $password_confirmation != null && $password != $password_confirmation) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Mật khẩu xác nhận không khớp!</strong></span><br>';
    }
    if ($username != null && !preg_match("/^[a-zA-Z0-9_-]{5,16}$/", $username)) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không hợp lệ!</strong></span><br>';
    }
    if ($email != null && !preg_match('/^.+@.+$/', $email)) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Định dạng email không hợp lệ!</strong></span><br>';
    }
    $sql = "SELECT `user_id` FROM `user` WHERE `email` = '".$game->real_escape_string($email)."';";
    if ($game->query($sql)->num_rows > 0) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Email này đã có người sử dụng!</strong></span><br>';
    }
    $sql = "SELECT `user_id` FROM `user` WHERE `user` = '" . $game->real_escape_string($username) . "' LIMIT 1;";
    if ($game->query($sql)->num_rows > 0) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản này đã có người sử dụng!</strong></span><br>';
    }
    if (!$isError) {
      $sql = "INSERT INTO `user` (`user`, `password`, `email`, `token`, `active`) VALUES ('". $game->real_escape_string($username) ."', '". $game->real_escape_string($password) ."', '". $game->real_escape_string($email) . "', '". $game->real_escape_string($token) . "', 1)";
      if ($game->query($sql)) {
        $success = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Đăng ký tài khoản thành công.</strong></span><br>';
      } else {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Lỗi! Không thể thêm tài khoản vào CSDL.</strong></span><br>';
      }
    }
}
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Đăng Ký</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="/register" method="post">
        <span style="margin-left:-85px; font-family: AVO, Arial !important;">Tên tài khoản</span><br>
        <input name="username" style="margin-top:3px; margin-bottom:5px"><br>
        <span style="margin-left:-130px; font-family: AVO, Arial !important;">Email</span><br>
        <input name="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-105px;font-family: AVO, Arial !important;"> Mật khẩu </span><br>
        <input name="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-52px;font-family: AVO, Arial !important;"> Nhập lại mật khẩu </span><br>
        <input name="password_confirmation" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <?php echo $error; echo $success; ?>
        <button name="register">Đăng ký</button><br><br>
        <p>Bạn đã có tài khoản? <a href="/login" style="color:red">Đăng nhập</a> ngay!</p>
      <form>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>