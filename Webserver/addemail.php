<?php
$title = "Thêm Email - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('location: /login');
  exit;
}
if ($user['email'] != null) {
  header('location: /profile');
  exit;
}
if ($_POST) {
  $email = $_POST['email'];
  $reemail = $_POST['reemail'];
  $password = $_POST['password'];
  if ($email == null || $reemail == null || $password == null) {
    $error = 'Không được bỏ trống!.';
  } else if (!preg_match('/^.+@.+$/', $email)) {
    $error = 'Email không hợp lệ!.';
  } else if (!preg_match('/^.+@.+$/', $reemail)) {
    $error = 'Email xác nhận không hợp lệ!.';
  } else if ($email != $reemail) {
    $error = 'Email xác nhận không khớp!.';
  } else if ($user['password'] != $password) {
    $error = 'Mật khẩu không chính xác!.';
  } else if ($game->query("SELECT `email` FROM `user` WHERE `email` = '".$game->real_escape_string($email)."';")->num_rows > 0) {
    $error = '<div class="error">Email đã tồn tại!.</div>';
  } else {
    $sql = "UPDATE `user` SET `email` = '".$game->real_escape_string($email)."' WHERE `user_id` = ".$_SESSION['uid']." AND `email` LIKE '';";
    $game->query($sql);
    if ($game->affected_rows > 0) {
      $success = 'Thêm email thành công!.<br />';
    } else {
      $error = 'Thêm thất bại!.';
    }
  }
} 
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Thêm Email</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="POST">
        <span style="margin-left:-122px; font-family: AVO, Arial !important;">Email:</span><br>
        <input name="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-70px;font-family: AVO, Arial !important;">Nhập lại Email:</span><br>
        <input name="reemail" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-99px;font-family: AVO, Arial !important;">Mật khẩu:</span><br>
        <input name="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <span class="invalid-feedback" role="alert" style="color:red"><strong><?php echo $error; ?></strong></span><br>
        <span class="invalid-feedback" role="alert" style="color:green"><strong><?php echo $success; ?></strong></span>
        <button name="them">Thêm</button><br><br>
      <form>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>