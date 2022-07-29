<?php
$title = "Thông Tin - MobiArmy II";
include './include/head.php';
include './include/function.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
if ($createdCharacter) {
  $nameCharacter = array("Gunner", "Miss 6", "Electrician", "King Kong", "Rocketer", "Granos", "Chicky" ,"Tarzan", "Apache", "Magenta");
  $nvstt = $armymem['sttnhanvat'];
  $characterPurchased = array();
  $ruongTB = json_decode($armymem['ruongTrangBi'], true);
  $len = count($ruongTB);
  $info = array();
  for ($i = 0; $i < 10; $i++) {
    $characterPurchased[$i] = ($nvstt & 1) > 0;
    $info[$i] = json_decode($armymem['NV'.($i + 1)], true);
    $nvstt = $nvstt / 2;
  }
}
?>
<div class="bg-content" style="text-align:right">Xin chào <?php echo $user['user'] ?>,
  <a href="/logout" style="color:seagreen"><b>Đăng xuất!</b></a>
</div>
<div class="bg-content content">
  <?php
    if (!$createdCharacter) {
      echo '<div class="bg-content" style="text-align:right">Vui lòng đăng nhập vào game để tạo nhân vật,<a href="/logout" style="color:seagreen"><b>Đăng xuất!</b></a></div></div>';
      include './include/end.php';
      exit();
    }
  ?>
  <h3 style="text-align: center"><b>THÔNG TIN TÀI KHOẢN</b></h3>
  <div class="title">
    <h4 style="font-size: 12px">CHỨC NĂNG</h4>
  </div>
  <ul>
    <li>
      <a style="color: #4e452a" href="/change-password" title="Đổi Mật Khẩu">Đổi Mật Khẩu</a>
    </li>
    <li>
      <a style="color: #4e452a" href="/nap-tien" title="Nạp Tiền">Nạp Tiền</a>
    </li>
    <? if ($user['email'] == null ) { ?>
    <li>
      <a style="color: #4e452a" href="/addemail" title="Thêm email">Thêm Email</a>
    </li>
    <? } else { ?>
    <li>
      <a style="color: #4e452a" href="/changeemail" title="Đổi email">Đổi Email</a>
    </li>
    <? }
    if ($user['password2'] == '-1') { ?>
    <li>
      <a style="color: #4e452a" href="/addsignature" title="Thêm chữ ký">Thêm Chữ Ký</a>
    </li>
    <? } else { ?>
    <li>
      <a style="color: #4e452a" href="/changesignature" title="Đổi chữ ký">Đổi Chữ Ký</a>
    </li>
    <? }
    if ($user['password2'] != '-1' && $user['email'] != null) { ?>
    <li>
      <a style="color: #4e452a" href="/forgotsignature" title="Quên chữ ký">Quên Chữ Ký</a>
    </li>
    <? } ?>
    
    
    <li>
      <a style="color: #4e452a" href="/equip" title="Trang Bị">Mặc/Tháo Trang Bị</a>
    </li>
    <li>
      <a style="color: #4e452a" href="/addpoint" title="Cộng điểm">Cộng Điểm Nâng Cấp</a>
    </li>
    <li>
      <a style="color: #4e452a" href="/shop" title="shop">Shop Account</a>
    </li>
  </ul>
  <div class="title">
    <h4 style="font-size: 12px">THÔNG TIN CÁ NHÂN</h4>
  </div>
  <ul>
    <li><b>ID: </b> <?php echo $user['user_id'] ?></li>
    <li><b>Tài khoản: </b> <?php echo $user['user'] ?></li>
    <li><b>Xu: </b> <span style="color:red"><?php echo $armymem['xu'] ?></span></li>
    <li><b>Lượng: </b> <span style="color:red"><?php echo $armymem['luong'] ?></span></li>
    <li><b>Danh vọng: </b> <span style="color:red"><?php echo $armymem['dvong'] ?></span></li>
    <li><b>Tiền: </b> <span style="color:red"><?php echo $armymem['money'] ?></span></li>
    <li><b>Email: </b> <?php echo $user['email'] == null ? "Chưa có email" : $user['email'] ?></li>
     <li><b>Chữ ký: </b> <?php echo $user['password2'] == '-1' ? "Chưa có chữ ký" : replace_str($user['password2'], strlen($user['password2']) / 100 * 30); ?></li>
  </ul>
  <?php for ($i = 0; $i < 10; $i++) { 
    if (!$characterPurchased[$i]) {
      continue;
    }
    $nextXP = 500 * $info[$i]['lever'] * ($info[$i]['lever'] + 1);
    $leverPercen = $info[$i]['xp'] * 100 / $nextXP;
    $ability = array(0,0,0,0,0);
    $invAdd = array(0,0,0,0,0);
    $percenAdd = array(0,0,0,0,0);
    $NVData = $game->query("SELECT `sat_thuong` FROM `nhanvat` WHERE `nhanvat_id` = ".($i + 1)." LIMIT 1;")->fetch_assoc();
    $nv_st = $NVData['sat_thuong'];
    $dataEquip = $info[$i]['data'];
    for ($c = 0; $c < 6; $c++) {
      if ($dataEquip[$c] < 0 || $dataEquip[$c] > $len) {
        continue;
      }
      $eq = $ruongTB[$dataEquip[$c]];
      for ($d = 0; $d < 5; $d++) {
        $invAdd[$d] += $eq['invAdd'][$d];
        $percenAdd[$d] += $eq['percenAdd'][$d];
      }
    }
    $ability[0] = 1000 + $info[$i]['pointAdd'][0] * 10 + $invAdd[0] * 10;
    $ability[0] += (1000 + $info[$i]['pointAdd'][0]) * $percenAdd[0] / 100;
    $ability[1] = $nv_st * (100 + ($invAdd[1] + $info[$i]['pointAdd'][1]) / 3 + $percenAdd[1]) / 100;
    $ability[2] = ($invAdd[2] + $info[$i]['pointAdd'][2]) * 10;
    $ability[2] += $ability[2] * $percenAdd[2] / 100;
    $ability[3] = ($invAdd[3] + $info[$i]['pointAdd'][3]) * 10;
    $ability[3] += $ability[3] * $percenAdd[3] / 100;
    $ability[4] = ($invAdd[4] + $info[$i]['pointAdd'][4]) * 10;
    $ability[4] += $ability[4] * $percenAdd[4] / 100;
  ?>
  <div class="title">
    <h4 style="font-size: 12px">THÔNG TIN NHÂN VẬT <? echo $nameCharacter[$i]. ' <a onclick="view('.$user['user_id'].', '.$i.')" style="color:red">[Trang bị]</a>' ?></h4>
  </div>
  <table width="100%">
    <tr>
      <td width="20%" style="text-align:center">
        <?php echo '<img src="/'.$user['user_id'].'/'.($i + 1).'.png" />' ?>
      </td>
      <td width="80%" >
        <li><b>Cấp: </b> <span style="color:red"><?php echo $info[$i]['lever'] ?></span></li>
        <li><b>Kinh nghiệm: </b> <span style="color:red"><?php echo $info[$i]['xp'].'/'.$nextXP.' ('.(int) $leverPercen.'%)'; ?></span></li>
        <li><b>Sinh lực: </b> <span style="color:red"><?php echo (int) $ability[0] ?></span></li>
        <li><b>Sức mạnh: </b> <span style="color:red"><?php echo (int) $ability[1] ?></span></li>
        <li><b>Phòng thủ: </b> <span style="color:red"><?php echo (int) $ability[2] ?></span></li>
        <li><b>May mắn: </b> <span style="color:red"><?php echo (int) $ability[3] ?></span></li>
        <li><b>Đồng đội: </b> <span style="color:red"><?php echo (int) $ability[4] ?></span></li>
      </td>
    </tr>
  </table>
  <?php } ?>
</div>
<?php
include './include/end.php';
?>