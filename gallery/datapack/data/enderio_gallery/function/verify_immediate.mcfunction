function enderio_gallery:verify
scoreboard players operation #immediate_failures enderio_gallery = #failures enderio_gallery
scoreboard players operation #immediate_checked enderio_gallery = #checked enderio_gallery
tellraw @a [{"text":"Ender IO gallery immediate: "},{"score":{"name":"#immediate_checked","objective":"enderio_gallery"}},{"text":" checks, "},{"score":{"name":"#immediate_failures","objective":"enderio_gallery"}},{"text":" failures"}]
