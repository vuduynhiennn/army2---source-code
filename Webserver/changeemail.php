<?php
$title = "Đổi Email - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('location: /login');
  exit;
}
if ($user['email'] == null) {
  header('location: /profile');
  exit;
}
if ($_POST) {
    $email                = $_POST['email'];
    $reemail   = $_POST['reemail'];
    if ($email == null || $reemail == null) {
      $error = 'Không được bỏ trống!.';
    } else if ($reemail != $email) {
      $error = 'Email xác nhận không khớp!.';
    } else if (!preg_match('/^.+@.+$/', $reemail)) {
    $error = 'Email xác nhận không hợp lệ!.';
    } else if ($user['email'] == $email) {
    $error = 'Vui lòng không nhập email hiện tại!.';
    } else if ($game->query("SELECT `email` FROM `user` WHERE `email` = '".$game->real_escape_string($email)."';")->num_rows > 0) {
    $error = 'Email đã tồn tại!.';
    } else {
      include './include/sendmail.php';
      $token = openssl_random_pseudo_bytes(20);
      $token = bin2hex($token);
      $filename = "./public/email.html";
      $fp = fopen($filename, "r");
      $contents = fread($fp, filesize($filename));
      $contents = str_replace("mail_content", "Email mới là: ".$email, $contents);
      $contents = str_replace("button_name", "Xác nhận", $contents);
      $contents = str_replace("url", $config['site']."/verification.php?type=changeemail&email=".$email."&token=".$token."&username=".$user['user'], $contents);
      fclose($fp);
      $game->query("UPDATE `user` SET `token` = '".$token."' WHERE `user_id` = ".$user['user_id'].";");
      if ($game->affected_rows > 0) {
        $req = sendMail($user['email'], 'Đổi email', $contents);
        if ($req['status']) {
          $success = 'Vui lòng kiểm tra email để xác nhận thay đổi.<br />';
        } else {
          $error = $req['message'];
        }
      } else {
        $error = 'Có lỗi xảy ra!.';
      }
    }
  }
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Đổi Email</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="POST">
        <span style="margin-left:-122px; font-family: AVO, Arial !important;">Email:</span><br>
        <input name="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <span  style="margin-left:-70px;font-family: AVO, Arial !important;">Nhập lại Email:</span><br>
        <input name="reemail" type="email" style="margin-top:3px; margin-bottom:5px"><br>
        <span class="invalid-feedback" role="alert" style="color:red"><strong><?php echo $error; ?></strong></span><br>
        <span class="invalid-feedback" role="alert" style="color:green"><strong><?php echo $success; ?></strong></span>
        <button name="change">Đổi</button><br><br>
      <form>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>