<?php
  $title = "Đăng nhập - MobiArmy II";
  include './include/head.php';
  include './include/connect.php';
  if (isset($_SESSION['user_id'])) {
    header('Location: /profile');
    exit;
  }
  $error = '';
  $isError = false;
  if (isset($_POST['login'])) {
    $username              = $_POST['username'];
    $password              = $_POST['password'];
    if ($username == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không được bỏ trống!</strong></span><br>';
    }
    if ($password == null) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Mật khẩu không được bỏ trống!</strong></span><br>';
    }
    if ($username != null && !preg_match("/^[a-zA-Z0-9_-]{5,16}$/", $username)) {
      $isError = true;
      $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không hợp lệ!</strong></span><br>';
    }
    if (!$isError) {
      $sql = "SELECT `user_id`, `active` FROM `user` WHERE `user` = '".$game->real_escape_string($username)."' AND `password` = '".$game->real_escape_string($password)."' LIMIT 1;";
      if ($game->query($sql)->num_rows > 0) {
        $info = $game->query($sql)->fetch_assoc();
        if ($info['active'] == 0) {
          $error = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Vui lòng kiểm tra hòm thư để kích hoạt tài khoản!</strong></span><br>';
        } else {
          $sql = "SELECT `id` FROM `armymem` WHERE `id` = ".$info['user_id']." LIMIT 1";
          if ($game->query($sql)->num_rows > 0) {
            $_SESSION['user_id'] = $info['user_id'];
            $_SESSION['uid'] = $info['user_id'];
            header('Location: /profile');
            exit;
          } else {
            $error = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Vui lòng đăng nhập vào game để tạo nhân vật!</strong></span><br>';
          }
        }
      } else {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản hoặc mật khẩu không chính xác!</strong></span><br>';
      }
    }
  }
?>
                
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Đăng Nhập</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="/login" method="POST">
        <span style="margin-left:-85px; font-family: AVO, Arial !important;">Tên tài khoản</span><br>
        <input name="username" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-105px;font-family: AVO, Arial !important;"> Mật khẩu </span><br>
        <input name="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <?php echo $error; ?>
        <button name="login">Đăng nhập</button><br><br>
        <a href="/forgot-password" style="color:red">Bạn quên mật khẩu?</a>
        <p>Bạn chưa có tài khoản? <a href="/register" style="color:red">Đăng ký</a> ngay</p>
      <form>
    </div>
  </div>
</div>
<?php
  include './include/end.php';
?>