
<?php 
  $title = "Xếp hạng - MobiArmy II"; 
  include './include/head.php';
  include './include/connect.php';
  $sql = "SELECT `user`.`user`, `armymem`.`id`, `armymem`.`xpMax` FROM `armymem` INNER JOIN `user` ON `armymem`.`id` = `user`.`user_id` ORDER BY `armymem`.`xpMax` DESC LIMIT 10;";
  $top_xp = $game->query($sql);
  $sql = "SELECT `user`.`user`, `armymem`.`id`, `armymem`.`xu` FROM `armymem` INNER JOIN `user` ON `armymem`.`id` = `user`.`user_id` ORDER BY `armymem`.`xu` DESC LIMIT 10;";
  $top_xu = $game->query($sql);
  $sql = "SELECT `user`.`user`, `armymem`.`id`, `armymem`.`luong` FROM `armymem` INNER JOIN `user` ON `armymem`.`id` = `user`.`user_id` ORDER BY `armymem`.`luong` DESC LIMIT 10;";
  $top_luong = $game->query($sql);
  $sql = "SELECT `user`.`user`, `armymem`.`id`, `armymem`.`dvong` FROM `armymem` INNER JOIN `user` ON `armymem`.`id` = `user`.`user_id` ORDER BY `armymem`.`dvong` DESC LIMIT 10;";
  $top_dvong = $game->query($sql);
  $sql = "SELECT `name`, `icon`, `xp`, `level` FROM `clan` ORDER BY `level` DESC LIMIT 10;";
  $top_clan = $game->query($sql);
?>
<div class="bg-content">
  <div class="title">
    <h4>Xếp Hạng Biệt Đội</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%">
	    <tbody>
		    <tr>
		       <td width="10%" style="text-align:center;background:#C0C0C0"><strong>Icon</strong></td>
			    <td width="50%" style="text-align:center;background:#C0C0C0"><strong>Tên</strong></td>
			    <td width="20%" style="text-align:center;background:#C0C0C0"><strong>Cấp</strong></td>
			    <td width="20%" style="text-align:center;background:#C0C0C0"><strong>Kinh nghiệm</strong></td>
		    </tr>
		    <?php while ($clan = $top_clan->fetch_assoc()) { ?>
		    <tr>
		      <td style="text-align:center;background:white"><img src="/team/res/icon/<?php echo $clan['icon'] ?>.png"></td>
		      <td style="text-align:center;background:white"><?php echo $clan['name'] ?></td>
		      <td style="text-align:center;background:white"><?php echo $clan['level'] ?></td>
		      <td style="text-align:center;background:white"><?php echo $clan['xp'] ?></td>
		    </tr>
		    <?php } ?>
		  </tbody>
    </table>
  </div>
</div>
<div class="bg-content">
  <div class="title">
    <h4>Xếp Hạng Cao Thủ</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%">
	    <tbody>
		    <tr>
			    <td width="30%" style="text-align:center;background:#C0C0C0"><strong>Tên</strong></td>
			    <td style="text-align:center;background:#C0C0C0"><strong>Kinh nghiệm</strong></td>
		    </tr>
		    <?php while ($mem = $top_xp->fetch_assoc()) { ?>
		    <tr>
		      <td style="text-align:center;background:white"><?php echo $mem['user'] ?></td>
		      <td style="text-align:center;background:white"><?php echo $mem['xpMax'] ?></td>
		    </tr>
		    <?php } ?>
		  </tbody>
    </table>
  </div>
</div>
<div class="bg-content">
  <div class="title">
    <h4>Xếp Hạng Xu</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%">
	    <tbody>
		    <tr>
			    <td width="30%" style="text-align:center;background:#C0C0C0"><strong>Tên</strong></td>
			    <td style="text-align:center;background:#C0C0C0"><strong>Xu</strong></td>
		    </tr>
		    <?php while ($mem = $top_xu->fetch_assoc()) { ?>
		    <tr>
		      <td style="text-align:center;background:white"><?php echo $mem['user'] ?></td>
		      <td style="text-align:center;background:white"><?php echo $mem['xu'] ?></td>
		    </tr>
		    <?php } ?>
		  </tbody>
    </table>
  </div>
</div>
<div class="bg-content">
  <div class="title">
    <h4>Xếp Hạng Lượng</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%">
    	    <tbody>
    		    <tr>
    			    <td width="30%" style="text-align:center;background:#C0C0C0"><strong>Tên</strong></td>
    			    <td style="text-align:center;background:#C0C0C0"><strong>Lượng</strong></td>
    		    </tr>
    		    <?php while ($mem = $top_luong->fetch_assoc()) { ?>
    		    <tr>
    		      <td style="text-align:center;background:white"><?php echo $mem['user'] ?></td>
    		    	<td style="text-align:center;background:white"><?php echo $mem['luong'] ?></td>
    		    </tr>
    		    <?php } ?>
    		  </tbody>
        </table>
  </div>
</div>
<div class="bg-content">
  <div class="title">
    <h4>Xếp Hạng Danh Vọng</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%">
    	    <tbody>
    		    <tr>
    			    <td width="30%" style="text-align:center;background:#C0C0C0"><strong>Tên</strong></td>
    			    <td style="text-align:center;background:#C0C0C0"><strong>Danh vọng</strong></td>
    		    </tr>
    		    <?php while ($mem = $top_dvong->fetch_assoc()) { ?>
    		    <tr>
    		      <td style="text-align:center;background:white"><?php echo $mem['user'] ?></td>
    		      <td style="text-align:center;background:white"><?php echo $mem['dvong'] ?></td>
    		    </tr>
    		    <?php } ?>
    		  </tbody>
        </table>
  </div>
</div>



<!--footer-->
	</div>
                            <br>
                        </div>
                        <br>

                    </div>
                </div>
            </div>
            <div class="bg_tree"></div>
            
<?php include './include/end.php'; ?>