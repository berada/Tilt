package hu.bme.aut.android.tilt.model

import android.util.Log

object TiltModel {

    //Stage, azaz pálya elemek
    const val EMPTY: Short = 0
    const val RED_PUCK: Short = 1
    const val GREEN_PUCK: Short = 2
    const val WALL: Short = 3
    const val HOLE: Short = 4
    //irányok
    const val LEFT: Short = 5
    const val RIGHT: Short = 6
    const val UP: Short = 7
    const val DOWN: Short = 8
    //játék során felhasznált változók
    var isLost: Boolean = false
    var isEnd: Boolean = false
    var moves: Int = 0
    var model: Array<ShortArray> = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY))

    fun resetModel() {
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                model[x][y] = EMPTY
            }
        }
    }

    fun getModelString(model: Array<ShortArray>): String {
        var text: String = ""
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                text += getFieldContent(x,y).toString()
            }
        }
        return text
    }

    fun loadStage(stage: String) {
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                setFieldContent(x,y,stage.substring(x*5+y, x*5+y+1).toShort())
            }
        }
    }

    fun initGame(stage: String) {
        loadStage(stage)
        isEnd = false
        isLost = false
        moves = 0
    }

    fun getFieldContent(x: Int, y: Int): Short {
        return model[x][y]
    }

    fun setFieldContent(x: Int, y: Int, content: Short): Short {
        model[x][y] = content
        return content
    }

    fun isFinished(): Boolean {
        if (isEnd) {
            return true
        }
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                if (getFieldContent(x,y) == GREEN_PUCK) {
                    return false
                }
            }
        }
        //ha nincs zöld korong a pályán akkor vége
        return true
    }

    fun moveLeft(){
        moves += 1
        var tmpType: Short
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                if (getFieldContent(x,y) == GREEN_PUCK || getFieldContent(x,y) == RED_PUCK){
                    //"felvesszük" és "lerakjuk" az új helyére a korongot, ehhez kell tudnunk, hogy milyen korongot "vettünk fel"
                    tmpType = getFieldContent(x,y)
                    for (i in x-1 downTo 0) {
                        //ha fal vagy másik korong van tőle balra, akkor attól közvetlenül balra kerül
                        if (getFieldContent(i,y) == GREEN_PUCK || getFieldContent(i,y) == RED_PUCK || getFieldContent(i,y) == WALL) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(i + 1,y,tmpType)
                            break
                        }
                        //ha a pálya középi lyuk van tőlre balra, akkor abba "belesik", lekerül a pályáról
                        //ha piros korong, akkor vége a játéknak: vesztettünk
                        if (getFieldContent(i,y) == HOLE) {
                            //ha piros korong kerül a lyukba, akkor vége vesztettünk
                            if (tmpType == RED_PUCK) {
                                isLost = true
                                isEnd = true
                            }
                            //a zöld korong lekerül a páyláról
                            setFieldContent(x,y, EMPTY)
                            Log.e("SWIPE LEFT HOLE OLD", "x: $x, y: $y, type: ${getFieldContent(x,y)}")
                            //ellenőrizzük, hogy van-e még zöld korong a pályán, ha nincs, akkor vége a játéknak
                            isEnd = isFinished()
                            break
                        }
                        //ha nem volt tőle balra semmi egyik mezőn se, akkor a pálya szélére kerül a korong
                        if (i == 0) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(0,y,tmpType)
                        }
                    }
                }
            }
        }
    }

    fun moveUp(){
        moves += 1
        var tmpType: Short
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                if (getFieldContent(x,y) == GREEN_PUCK || getFieldContent(x,y) == RED_PUCK){
                    tmpType= getFieldContent(x,y)
                    for (i in y-1 downTo 0) {
                        if (getFieldContent(x,i) == GREEN_PUCK || getFieldContent(x,i) == RED_PUCK || getFieldContent(x,i) == WALL) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(x,i + 1,tmpType)
                            break
                        }
                        if (getFieldContent(x,i) == HOLE) {
                            if (tmpType == RED_PUCK) {
                                isLost = true
                                isEnd = true
                            }
                            setFieldContent(x,y, EMPTY)
                            isEnd = isFinished()
                            break
                        }
                        if (i == 0) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(x,0,tmpType)
                        }
                    }
                }
            }
        }
    }

    fun moveRight(){
        moves += 1
        var tmpType: Short
        for (x in 4 downTo 0) {
            for (y in 4 downTo 0) {
                if (getFieldContent(x,y) == GREEN_PUCK || getFieldContent(x,y) == RED_PUCK){
                    tmpType = getFieldContent(x,y)
                    for (i in x + 1 until 5) {
                        if (getFieldContent(i,y) == GREEN_PUCK || getFieldContent(i,y) == RED_PUCK || getFieldContent(i,y) == WALL) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(i - 1,y,tmpType)
                            break
                        }
                        if (getFieldContent(i,y) == HOLE) {
                            if (tmpType == RED_PUCK) {
                                isLost = true
                                isEnd = true
                            }
                            setFieldContent(x,y, EMPTY)
                            isEnd = isFinished()
                            break
                        }
                        if (i == 4) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(4,y,tmpType)
                        }
                    }
                }
            }
        }
    }

    fun moveDown(){
        moves += 1
        var tmpType: Short
        for (x in 4 downTo 0) {
            for (y in 4 downTo 0) {
                if (getFieldContent(x,y) == GREEN_PUCK || getFieldContent(x,y) == RED_PUCK){
                    tmpType = getFieldContent(x,y)
                    for (i in y + 1 until 5) {
                        if (getFieldContent(x,i) == GREEN_PUCK || getFieldContent(x,i) == RED_PUCK || getFieldContent(x,i) == WALL) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(x,i - 1,tmpType)
                            break
                        }
                        if (getFieldContent(x,i) == HOLE) {
                            if (tmpType == RED_PUCK) {
                                isLost = true
                                isEnd = true
                            }
                            setFieldContent(x,y, EMPTY)
                            isEnd = isFinished()
                            break
                        }
                        if (i == 4) {
                            setFieldContent(x,y, EMPTY)
                            setFieldContent(x,4,tmpType)
                        }
                    }
                }
            }
        }
    }

    fun move(dir: Short) {
        when (dir) {
            LEFT -> moveLeft()
            RIGHT -> moveRight()
            UP -> moveUp()
            DOWN -> moveDown()
        }
    }

}