<?php
$title = "Quản Lý Tài Khoản - MobiArmy II";
include '../include/head.php';
include '../include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
$page = $_GET['page'];
if ($page == null || !preg_match('/^[0-9]+$/', $page) || $page < 1) {
  $page = 1;
}
$rows = (int) $game->query("SELECT COUNT(*) AS `total` FROM `shop` WHERE `user_id` = ".$user['user_id']." AND `buy` != 2;")->fetch_assoc()['total'];
$num_page = ($rows / 10) + 1;
$start = ($page - 1) * 10;
$list = $game->query("SELECT * FROM `shop` WHERE `user_id` = ".$user['user_id']." AND `buy` != 2 LIMIT $start, 10;");
?>
<div class="bg-content">
  <div class="content">
    <h3 style="text-align: center"><b>QUẢN LÝ ACCOUNT</b></h3>
    <div class="title">
      <h4>Chức Năng</h4>
    </div>
      <ul>
        <li>
          <a style="color: #4e452a" href="/shop/dang-ban" title="band">Đăng Bán Tài Khoản</a>
        </li>
        <li>
          <a style="color: #4e452a" href="/shop" title="shop">Về Shop</a>
        </li>
        <li>
          <a style="color: #4e452a" title="shop">Bạn đang có: <? echo $armymem['money']; ?> đồng</a><a style="color: red" href="https://www.facebook.com/HuynhDuc.231" title="shop"> Rút tiền</a>
          </li>
      </ul>
  </div>
  <div class="content">
    <div class="title">
      <h4>Danh Sách Tài Khoản</h4>
    </div>
      <?php while ($acc = $list->fetch_assoc()) { 
     	  $nguoiban = $game->query("SELECT `user` FROM `user` WHERE `user_id` = ".$acc['user_id']." LIMIT 1;")->fetch_assoc();
        $taikhoan = $game->query("SELECT * FROM `user`, `armymem` WHERE `user`.`user_id` = `armymem`.`id` AND `user`.`user_id` = ".$acc['account_id']." LIMIT 1;")->fetch_assoc();
        $lv = 0;
        for ($i = 0; $i < 10; $i++) {
          $d = json_decode($taikhoan['NV'.$i], true);
          if ($d['lever'] > $lv) {
            $lv = $d['lever'];
          }
        }
        $buy = $acc['buy'] == 1 ? '<a style="color:red">[đã bán]</a>' : '<a style="color:green">[chưa bán]</a>';
        if ($acc['buy'] == 0) {
      ?>
      <p class="gmenu"><? echo $acc['id']; ?>. <? echo $buy; ?><a style="color:blue" href="/shop/<? echo $acc['account_id']; ?>/account"><b><? echo $taikhoan['user'] ?></b> lv:<? echo $lv; ?></a> giá: <? echo $acc['price'] ?> đồng -> <a href="/shop/<? echo $acc['id'] ?>/delete" style="color:blue" phdr="Xoá">Xoá</a> <a href="/shop/<? echo $acc['id'] ?>/edit" style="color:green" phdr="Sửa">Sửa</a></p>
      <? } else { ?>
        <p class="gmenu"><? echo $acc['id']; ?>. <? echo $buy; ?><a style="color:blue" href="/shop/<? echo $acc['account_id']; ?>/account"><b><? echo $taikhoan['user'] ?></b> lv:<? echo $lv; ?></a> giá: <? echo $acc['price'] ?> đồng</p>
      <? } 
      } ?>
  </div>
  <?
    if ($num_page > 1) {
      echo '<div class="pagination">';
      for ($i = 1; $i < $num_page; $i++) {
        if ($page == $i) {
          echo '<a class="active">'.$i.'</a>';
        } else {
          echo '<a href="/'.$i.'/shop/">'.$i.'</a>';
        }
      }
      echo '</div><br><br>';
    }
  ?>
  <div id="showForm"></div>
</div>
	
<?php
include '../include/end.php';
?>