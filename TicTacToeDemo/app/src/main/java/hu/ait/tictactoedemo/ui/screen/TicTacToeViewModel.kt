package hu.ait.tictactoedemo.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel



enum class Player { X, O }
data class BoardCell(val row: Int, val col: Int)

class TicTacToeViewModel : ViewModel() {
    var board by mutableStateOf(Array(3) {
        Array(3) { null as Player? } }
    )
        private set



    var currentPlayer by mutableStateOf(Player.X)
        private set

    var winner by mutableStateOf<Player?>(null)
        private set

    init {
        //board[0][0]= Player.X
    }

    fun onCellClicked(cell: BoardCell) {
        if (board[cell.row][cell.col] != null || winner != null) return

        //board[cell.row][cell.col] = currentPlayer
        val newBoard = board.copyOf()
        newBoard[cell.row][cell.col] = currentPlayer
        board = newBoard

        if (checkWin()) {
            winner = currentPlayer
        } else {
            currentPlayer =
                if (currentPlayer == Player.X) Player.O else Player.X
        }
    }

    fun resetGame() {
        board = Array(3) { Array(3) { null as Player? } }
        currentPlayer = Player.X
        winner = null
    }

    private fun checkWin(): Boolean {
        return false
    }

}