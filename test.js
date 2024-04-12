let length = 31
let pillarSize = 6
let pillar = 0
let modifier = 1/pillarSize
length *= modifier
let result = (length-pillar)/(pillar+1)
while (result > 1) {
    pillar++
    result = (length-pillar)/(pillar+1)
    if (result==Math.floor(result)) {
        console.log(pillar+" : "+result/modifier)
    }
}