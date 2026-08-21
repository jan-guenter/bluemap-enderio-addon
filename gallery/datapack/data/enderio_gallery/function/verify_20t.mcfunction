function enderio_gallery:verify
scoreboard players operation #20t_failures enderio_gallery = #failures enderio_gallery
scoreboard players operation #20t_checked enderio_gallery = #checked enderio_gallery
tellraw @a [{"text":"Ender IO gallery 20t: "},{"score":{"name":"#20t_checked","objective":"enderio_gallery"}},{"text":" checks, "},{"score":{"name":"#20t_failures","objective":"enderio_gallery"}},{"text":" failures"}]
