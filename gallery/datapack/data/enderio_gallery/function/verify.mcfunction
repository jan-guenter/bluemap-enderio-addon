scoreboard players set #failures enderio_gallery 0
scoreboard players set #checked enderio_gallery 0
# Supported painted stone and matching control
execute unless block 164 100 164 enderio:painted_redstone_block run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless data block 164 100 164 {id:"enderio:single_painted",Paint:"minecraft:stone"} run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless block 168 100 164 minecraft:stone run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
# Supported painted bricks and matching control
execute unless block 164 100 170 enderio:painted_redstone_block run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless data block 164 100 170 {id:"enderio:single_painted",Paint:"minecraft:bricks"} run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless block 168 100 170 minecraft:bricks run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
# Supported painted oak planks and matching control
execute unless block 164 100 176 enderio:painted_redstone_block run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless data block 164 100 176 {id:"enderio:single_painted",Paint:"minecraft:oak_planks"} run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless block 168 100 176 minecraft:oak_planks run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
# Malformed host: correct block entity but no top-level Paint
execute unless block 172 100 164 enderio:painted_redstone_block run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless data block 172 100 164 {id:"enderio:single_painted"} run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute if data block 172 100 164 Paint run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
# Unsupported host: exact Paint persists but strict model proof rejects stairs
execute unless block 172 100 170 enderio:painted_redstone_block run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless data block 172 100 170 {id:"enderio:single_painted",Paint:"minecraft:oak_stairs"} run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
execute unless score #builds enderio_gallery matches 1 run scoreboard players add #failures enderio_gallery 1
scoreboard players add #checked enderio_gallery 1
