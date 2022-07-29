<?php
$title = "Bán Tài Khoản - MobiArmy II";
include '../include/head.php';
include '../include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
if ($_POST) {
  $account      = $_POST['account'];
  $password     = $_POST['password'];
  $email        = $_POST['email'];
  $signature    = $_POST['signature'];
  $price        = $_POST['price'];
  if ($account == null || $password == null || $email == null || $signature == null || $price == null) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được bỏ trống!</strong></span><br>';
  } else if (!preg_match('/^.+@.+$/', $email)) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Email không hợp lệ!</strong></span><br>';
  } else if (!preg_match('/^[a-zA-Z0-9_-]{5,16}$/', $account)) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không hợp lệ!</strong></span><br>';
  } else if (!preg_match('/^[0-9]+$/', $price)) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Giá bán không hợp lệ!</strong></span><br>';
  } else if ($price < 1000 || $price > 1000000) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Giá bán tối thiểu 1000 và tối đa 1000000!</strong></span><br>';
  } else if ($user['user'] == $account) {
    $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Bạn không thể dùng tài khoản này để bán tài khoản này!</strong></span><br>';
  } else {
    $sql = "SELECT `user_id`, `password2`, `email` FROM `user` WHERE `user` = '".$game->real_escape_string($account)."' AND `password` = '".$game->real_escape_string($password)."' AND `email` = '".$game->real_escape_string($email)."' AND `password2` = '".$game->real_escape_string($signature)."' LIMIT 1";
    $r = $game->query($sql);
    if ($r->num_rows == 1) {
      $r = $r->fetch_assoc();
      if ($r['password2'] == '-1') {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không có chữ ký!</strong></span><br>';
      } else if ($r['email'] == null) {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản không có email!</strong></span><br>';
      } else if ($game->query("SELECT `user`.`user_id`, `shop`.`account_id` FROM `user` INNER JOIN `shop` ON `user`.`user_id` = `shop`.`account_id` WHERE `shop`.`buy` = 0 AND `user`.`user_id` = ".$r['user_id']." LIMIT 1;")->num_rows == 0) {
        if ($game->query("INSERT INTO `shop` (`id`, `user_id`, `account_id`, `price`, `buy`) VALUES ('', '".$user['user_id']."', '".$r['user_id']."', '".$price."', '0');")) {
          $error = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Đăng bán thành công!</strong></span><br>';
        } else {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Đăng bán thất bại!</strong></span><br>';
        }
      } else {
        $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Tài khoản này đã có người đăng bán!</strong></span><br>';
      }
    } else {
      $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Thông tin tài khoản cần bán không đúng!</strong></span><br>';
    }
  }
}
?>
<div class="bg-content">
  <div class="title">
    <h4>Chức Năng</h4>
  </div>
  <ul>
    <li>
      <a style="color: #4e452a" href="/shop/quan-ly" title="quản lý">Quản Lý Tài Khoản</a>
    </li>
    <li>
      <a style="color: #4e452a" href="/shop" title="band">Về Shop</a>
    </li>
  </ul>

  <div class="title">
    <h4>Bán Tài Khoản</h4>
  </div>
  <div class="content" style="text-align:center">
      <? echo $error; ?>
    <form action="" method="POST">
      <span style="margin-left:-100px; font-family: AVO, Arial !important;">Tài khoản:</span><br>
      <input name="account" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-102px;font-family: AVO, Arial !important;">Mật khẩu:</span><br>
      <input name="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-126px;font-family: AVO, Arial !important;">Email:</span><br>
      <input name="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-116px;font-family: AVO, Arial !important;">Chữ ký:</span><br>
      <input name="signature" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-135px;font-family: AVO, Arial !important;">Giá:</span><br>
      <input name="price" type="number" style="margin-top:3px; margin-bottom:5px"><br>
      <button name="change">Bán</button><br><br>
    <form>
  </div> 
</div>
	
<?php
include '../include/end.php';
?>