<?php
$title = "Thêm Chữ Ký - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
if ($user['password2'] != '-1') {
  header('location: /profile');
  exit;
}
if ($_POST) {
  $signature = $_POST['signature'];
  $resignature = $_POST['resignature'];
  $password = $_POST['password'];
  if ($signature == null || $resignature == null || $password == null) {
    $error = 'Không được bỏ trống!.';
  } else if ($signature != $resignature) {
    $error = 'Nhập lại chữ ký không khớp!.';
  } else if ($user['password'] != $password) {
    $error = 'Mật khẩu không chính xác!.';
  } else {
    $sql = "UPDATE `user` SET `password2` = '".$game->real_escape_string($signature)."' WHERE `user_id` = ".$_SESSION['uid']." AND `password2` LIKE '-1';";
    $game->query($sql);
    if ($game->affected_rows > 0) {
      $success = 'Thêm chữ ký thành công!.<br />';
    } else {
      $error = 'Thêm chữ ký thất bại!.';
    }
  }
} 
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Thêm Chữ Ký</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="POST">
        <span style="margin-left:-122px; font-family: AVO, Arial !important;">Chữ ký:</span><br>
        <input name="signature" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-70px;font-family: AVO, Arial !important;">Nhập lại chữ ký:</span><br>
        <input name="resignature" style="margin-top:3px; margin-bottom:5px"><br>
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