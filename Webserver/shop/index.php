<?php
$title = "Shop Account - MobiArmy II";
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
$rows = (int) $game->query("SELECT COUNT(*) AS `total` FROM `shop` WHERE `buy` = 0;")->fetch_assoc()['total'];
$num_page = ($rows / 10) + 1;
$start = ($page - 1) * 10;
$list = $game->query("SELECT * FROM `shop` WHERE `buy` = 0 LIMIT $start, 10;");
?>
<div class="bg-content">
  <div class="content">
    <h3 style="text-align: center"><b>SHOP ACCOUNT</b></h3>
    <div class="title">
      <h4>Chức Năng</h4>
    </div>
      <ul>
        <li>
          <a style="color: #4e452a" href="/shop/quan-ly" title="quản lý">Quản Lý Tài Khoản</a>
        </li>
        <li>
          <a style="color: #4e452a" href="/shop/dang-ban" title="band">Đăng Bán Tài Khoản</a>
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
      ?>
      <p id="element<? echo $acc['id']; ?>" class="gmenu"><? echo $acc['id']; ?>. <b><? echo $nguoiban['user'] ?></b> vừa giao bán nick: <a style="color:blue" href="/shop/<? echo $acc['account_id']; ?>/account"><b><? echo $taikhoan['user'] ?></b> lv:<? echo $lv; ?></a> với giá: <? echo $acc['price'] ?> đồng -> <a id="<? echo $acc['id'] ?>" style="color:blue" phdr="Mua ngay" onclick="formBuyAccount(this)">Mua ngay</a></p>
      <? } ?>
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