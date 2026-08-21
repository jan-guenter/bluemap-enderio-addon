scoreboard objectives add enderio_gallery dummy
scoreboard players add #builds enderio_gallery 1
function enderio_gallery:clear
forceload add 160 160 179 179
fill 160 99 160 179 99 179 minecraft:smooth_stone
# Supported natural-form persisted paint: stone
setblock 164 100 164 enderio:painted_redstone_block{Paint:"minecraft:stone"}
setblock 168 100 164 minecraft:stone
# Supported natural-form persisted paint: bricks
setblock 164 100 170 enderio:painted_redstone_block{Paint:"minecraft:bricks"}
setblock 168 100 170 minecraft:bricks
# Supported natural-form persisted paint: oak planks
setblock 164 100 176 enderio:painted_redstone_block{Paint:"minecraft:oak_planks"}
setblock 168 100 176 minecraft:oak_planks
function enderio_gallery:verify_immediate
schedule function enderio_gallery:verify_20t 20t replace
