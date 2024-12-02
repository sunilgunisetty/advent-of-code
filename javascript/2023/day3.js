const fs = require('fs')

const findTop = (row, col, no, rowLen, colLen, data) => {
    let startCol, endCol
    if (row === 0) return ''
    col === 0 ? startCol = 0 : startCol = col - 1
    col + no.length === colLen ? endCol = colLen - 1 : endCol = col + no.length
    return data[row - 1].substring(startCol, endCol + 1)

}

const findBottom = (row, col, no, rowLen, colLen, data) => {
    let startCol, endCol
    if (row === (rowLen - 1)) return ''
    col === 0 ? startCol = 0 : startCol = col - 1
    col + no.length === colLen ? endCol = colLen - 1 : endCol = col + no.length
    return data[row + 1].substring(startCol, endCol + 1)

}

const findLeft = (row, col, data) => {
    return col === 0 ? '' : data[row][col - 1]
}

const findRight = (row, col, no, colLength, data) => {
    return col + no.length === colLength ? '' : data[row][col + no.length]
}

const isValid = (chars) => !chars.split('').every((e) => e === '.' || !isNaN(e))

const findSurrounding = (contents, totalRows, totalCols, item) => {
    return {
        element: item.element,
        neighbors: [
            findTop(item.row, item.column, item.element, totalRows, totalCols, contents),
            findBottom(item.row, item.column, item.element, totalRows, totalCols, contents),
            findLeft(item.row, item.column, contents),
            findRight(item.row, item.column, item.element, totalCols, contents)
        ].filter(n => n)
            .map(isValid)
            .every(n => n === false)
    }
}


const day3Part1 = () => {
    const fileContents = fs
          .readFileSync('./input/day3-part1.txt', 'utf8')
          .split(/\r?\n/).filter(Boolean)

    const totalRows = fileContents.length
    const totalCols = fileContents[0].length

    const data = fileContents.flatMap((line, index) => {
        let res = [];
        let re = /\d+/g
        while((match = re.exec(line)) != null) {
            res.push({row: index, column: match.index, element: match[0]})
        }
        return res;
    })


    return data.map((item) => findSurrounding(fileContents,totalRows,totalCols,item))
        .filter( e => !e.neighbors)
        .map( e => parseInt(e.element))
        .reduce((e, acc) => e + acc, 0)
}

console.log("Day 3: Part 1: Solution: " + day3Part1());

// const fileData = fs
//       .readFileSync('./input/day3-sample.txt', 'utf8')
//       .split(/\r?\n/).filter(Boolean)

// console.log(findTop(2, 2, '35', 10, 10, fileData))
