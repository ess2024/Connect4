package connect4

import base.{AIMove, TreeNode}

import scala.util.control.Breaks.{break, breakable}

class MinimaxAI(maxDepth: Int, alphaBeta: Boolean) {

  private final val INF: Int = 1000
  var tree: TreeNode = _
  private var bestCol: Int = _
  private var nodesExpanded: Int = 0

  def cpuMove(board: IBoard): AIMove = {
    nodesExpanded = 0
    tree = new TreeNode()
    if (maxDepth > 0)
      maximizer(board, 0, 0, INF, tree)
    else
      breakable {
        for (c <- 0 until board.getCols)
          if (board.isValidRow(c)) {
            bestCol = c
            break()
          }
      }
    println(s"Nodes Expanded: $nodesExpanded")
    tree.column = bestCol + 1
    new AIMove(bestCol, tree)
  }

  private def minimizer(board: IBoard, depth: Int, lastBoardScore: Int, lastBoardBestScore: Int, node: TreeNode): Int = {
    nodesExpanded += 1
    if (depth == maxDepth)
      return 0

    var bestScore: Int = INF
    var nextNode: TreeNode = null
    breakable {
      for (c <- 0 until board.getCols) {
        if (board.isValidRow(c)) {

          // Tree representation info
          if (Constant.TREE_REPRESENTATION) {
            nextNode = new TreeNode(depth + 1, c + 1)
            node.children :+= nextNode
          }

          board.addPiece(c, Constant.YELLOW)
          val curBoardScore: Int = board.getHeuristicScore(c, Constant.YELLOW)
          val nextBoardScore: Int = maximizer(board, depth + 1, curBoardScore, bestScore, nextNode)
          val totalScore: Int = curBoardScore + nextBoardScore

          if (Constant.TREE_REPRESENTATION)
            nextNode.setScores(totalScore, curBoardScore, nextBoardScore)

          board.removePiece(c) // BackTracking
          bestScore = Math.min(bestScore, totalScore)

          if (alphaBeta && bestScore + lastBoardScore <= lastBoardBestScore)
            break
        }
      }
    }
    bestScore
  }

  private def maximizer(board: IBoard, depth: Int, lastBoardScore: Int, lastBoardBestScore: Int, node: TreeNode): Int = {
    nodesExpanded += 1
    if (depth == maxDepth)
      return 0

    var bestScore: Int = -INF
    var nextNode: TreeNode = null
    breakable {
      for (c <- 0 until board.getCols) {
        if (board.isValidRow(c)) {

          // Tree representation info
          if (Constant.TREE_REPRESENTATION) {
            nextNode = new TreeNode(depth + 1, c + 1)
            node.children :+= nextNode
          }

          board.addPiece(c, Constant.RED)
          val curBoardScore: Int = board.getHeuristicScore(c, Constant.RED)
          val nextBoardScore: Int = minimizer(board, depth + 1, curBoardScore, bestScore, nextNode)
          val totalScore: Int = curBoardScore + nextBoardScore

          if (Constant.TREE_REPRESENTATION)
            nextNode.setScores(totalScore, curBoardScore, nextBoardScore)

          board.removePiece(c) // BackTracking
          if (depth == 0 && totalScore > bestScore)
            bestCol = c

          bestScore = Math.max(bestScore, totalScore)
          if (alphaBeta && bestScore + lastBoardScore >= lastBoardBestScore)
            break
        }
      }
    }
    bestScore
  }
}
