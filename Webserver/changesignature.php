<?php
$title = "Đổi Chữ Ký - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
if ($user['password2'] == '-1') {
  header('location: /profile');
  exit;
}
$error   = '';
$success = '';
$isError = false;
if (isset($_POST['change'])) {
    $old_password          = $_POST['old_password'];
    $password              = $_POST['password'];
    $password_confirmation = $_POST['password_confirmation'];
    if ($old_password == null) {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống chữ ký cũ!</strong></span><br>';
    }
    if ($password == null) {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống chữ ký mới!</strong></span><br>';
    }
    if ($password_confirmation == null) {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được để trống chữ ký xác nhận!</strong></span><br>';
    }
    if ($password != null && $password_confirmation != null && $password != $password_confirmation) {
        $isError = true;
        $error .= '<span class="invalid-feedback" role="alert" style="color:red"><strong>Nhập lại chữ ký không khớp!</strong></span><br>';
    }
    if (!$isError) {
      $sql = "UPDATE `user` SET `password2` = '".$game->real_escape_string($password)."' WHERE `user_id` = ".$user_id." AND `password2` = '".$old_password."';";
      $game->query($sql);
      if ($game->affected_rows > 0) {
        $success = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Đổi chữ ký thành công!</strong></span><br>';
      } else {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Chữ ký cũ không chính xác!</strong></span><br>';
      }
    }
}
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Đổi Chữ Ký</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="post">
        <span style="margin-left:-85px; font-family: AVO, Arial !important;">Chữ ký cũ</span><br>
        <input name="old_password" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-79px;font-family: AVO, Arial !important;"> Chữ ký mới </span><br>
        <input name="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-52px;font-family: AVO, Arial !important;"> Nhập lại Chữ ký </span><br>
        <input name="password_confirmation" type="password" style="margin-top:3px; margin-bottom:5px"><br>
        <?php echo $error; echo $success; ?>
        <button name="change">Đổi</button><br><br>
      <form>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>