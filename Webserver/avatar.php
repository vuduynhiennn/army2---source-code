<?php
include './include/config.php';
include './include/connect.php';
include './lib/GifCreator.php';
use GifCreator\GifCreator as GifCreator;
$user_id = $_GET['user_id'];
$nvUsed = $_GET['nv'];
if ($user_id == null || !preg_match('/^[0-9]+$/',$user_id)) {
  $user_id = 1;
}
if ($nvUsed == null || !preg_match('/^[0-9]+$/',$nvUsed)) {
  $nvUsed = 1;
} else {
  if ($nvUsed < 1 || $nvUsed > 10) {
    $nvUsed = 1;
  }
}
$sql = "SELECT `ruongTrangBi`, `NV$nvUsed` FROM `armymem` WHERE `id` = ".$user_id." LIMIT 1;";
$mem = $game->query($sql)->fetch_assoc();
if ($mem == null) {
  header('location: /user/1.gif');
}
$nv = json_decode($mem['NV'.$nvUsed],true);
$ruongTB = json_decode($mem['ruongTrangBi'], true);
$data = $nv['data'];
$equip = array();
$nvId = $nvUsed -1;
$i = 0;
$equipDefault = array(2, 0, 1);
$head1 = array(
  array(13, 10, 0, 96, 24, 24), 
  array(13, 10, 0, 96, 24, 24),
  array(13, 10, 0, 96, 24, 24),
  array(13, 13, 0, 134, 30, 30), 
  array(13, 10, 0, 96, 24, 24),
  array(13, 10, 0, 96, 24, 24),
  array(13, 10, 0, 96, 24, 24), 
  array(13, 14, 0, 132, 32, 32),
  array(13, 10, 0, 96, 24, 24),
  array(13, 10, 0, 96, 24, 24)
);
$head2 = array(
  array(13, 10, 0, 120, 24, 24), 
  array(13, 10, 0, 120, 24, 24),
  array(13, 10, 0, 120, 24, 24),
  array(13, 12, 0, 165, 30, 30), 
  array(13, 10, 0, 120, 24, 24),
  array(13, 10, 0, 120, 24, 24),
  array(13, 11, 0, 120, 24, 24), 
  array(13, 14, 0, 164, 32, 32),
  array(13, 10, 0, 120, 24, 24),
  array(13, 10, 0, 120, 24, 24)
);
if ($data[5] >= 0) {
  $equipId = $ruongTB[$data[5]]['id'];
  $sql = "SELECT `arraySet` FROM `equip` WHERE `nv` = " . $nvId. " AND `equipId` = " . ($equipId) . " LIMIT 1;";
  $temp = $game->query($sql)->fetch_assoc();
  $arraySet = json_decode($temp['arraySet']);
  foreach ($arraySet as $temp_id) { 
        if ($temp_id == -1) {
			continue;
		}
		$sql = "SELECT `bigCutX`, `bigCutY`, `bigSizeX`, `bigSizeY`, `bigAlignX`, `bigAlignY` FROM `equip` WHERE `nv` = " . $nvId. " AND `equipId` = " . ($temp_id) . " LIMIT 1;";
		$equip[] = $game->query($sql)->fetch_assoc();
  }
} else {
  $j = -1;
  foreach ($data as $key => $value) {
    $equipId = $ruongTB[$value]['id'];
    $j++;
    if ($value == -1) {
      if ($nvId == 3) {
        continue;
      }
      if ($j < 3) {
        $equipId = $equipDefault[$j];
      } else {
        continue;
      }
    }
    $sql = "SELECT `bigCutX`, `bigCutY`, `bigSizeX`, `bigSizeY`, `bigAlignX`, `bigAlignY` FROM `equip` WHERE `nv` = " . $nvId. " AND `equipId` = " . $equipId . " LIMIT 1;";
    $equip[$i++] = $game->query($sql)->fetch_assoc();
  }
}
$dest = imagecreatefrompng('./public/images/blank.png');
$color = imagecolorallocate($dest, 0, 0, 255); 
imagefill($dest, 0, 0, $color);
imagecolortransparent($dest, $color);
$dest2 = imagecreatefrompng('./public/images/blank.png');
$color2 = imagecolorallocate($dest2, 0, 0, 255); 
imagefill($dest2, 0, 0, $color2);
imagecolortransparent($dest2, $color2);
$src = imagecreatefrompng('./public/images/player/'.$nvId.'.png');
$img = imagecreatefrompng('./public/images/bigImage'.$nvId.'.png');
foreach ($equip as $value) {
  $cutX = json_decode($value['bigCutX'], true);
  $cutY = json_decode($value['bigCutY'], true);
  $sizeX = json_decode($value['bigSizeX'], true);
  $sizeY = json_decode($value['bigSizeY'], true);
  $alignX = json_decode($value['bigAlignX'], true);
  $alignY = json_decode($value['bigAlignY'], true);
  imagecopymerge($dest, $img, 31 + $alignX[4], 50 + $alignY[4], $cutX[4], $cutY[4], $sizeX[4], $sizeY[4], 100);
  imagecopymerge($dest2, $img, 31 + $alignX[5], 50 + $alignY[5], $cutX[5], $cutY[5], $sizeX[5], $sizeY[5], 100);
  if (!$isHead) {
    $isHead = true;
    $he1 = $head1[$nvId];
    $he2 = $head2[$nvId];
    imagecopymerge($dest, $src, $he1[0], $he1[1], $he1[2], $he1[3], $he1[4], $he1[5], 100);
    imagecopymerge($dest2, $src, $he2[0], $he2[1], $he2[2], $he2[3], $he2[4], $he2[5], 100);
  }
}
if (count($equip) == 0) {
  $he1 = $head1[$nvId];
  $he2 = $head2[$nvId];
  imagecopymerge($dest, $src, $he1[0], $he1[1], $he1[2], $he1[3], $he1[4], $he1[5], 100);
  imagecopymerge($dest2, $src, $he2[0], $he2[1], $he2[2], $he2[3], $he2[4], $he2[5], 100);
}
$frames = array($dest,$dest2);
$durations = array(20, 20);
$gc = new GifCreator();
$gc->create($frames, $durations, 0);
$gifBinary = $gc->getGif();
header('Content-type: image/gif');
echo $gifBinary;
imagedestroy($src);
imagedestroy($img);
imagedestroy($dest);
imagedestroy($dest2);
?>

