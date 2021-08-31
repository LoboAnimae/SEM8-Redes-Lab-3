require('dotenv').config();
const xmpp = require('simple-xmpp');
const id = process.argv[2]?.toUpperCase() || 'A';
let acceptingNewMessagesYet = false;
let matrix = JSON.parse(process.env[id]);
const directNeighbors = Object.keys(matrix);
const possiblePaths = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'];

function findShortestPath(
    currentPathDestination,
    pathToNeighbor,
    neighborPathToDestination,
) {
    const possibleNewPath = pathToNeighbor + neighborPathToDestination;
    if (currentPathDestination < possibleNewPath) return currentPathDestination;
    if (currentPathDestination !== possibleNewPath)
        console.log(
            `Found a shorter path (${possibleNewPath} <= ${currentPathDestination})`,
        );
    return possibleNewPath;
}

function generateMatrix(foreignMatrixPath, matrixIdentifier) {
    const tempMatrix = { ...matrix };
    return new Promise((resolve, reject) => {
        if (!directNeighbors.includes(matrixIdentifier)) resolve(matrix);
        for (const path of possiblePaths) {
            if (path === id) continue;
            if (!(tempMatrix[path] && foreignMatrixPath[path])) {
                if (!foreignMatrixPath[path]) continue;
                tempMatrix[path] =
                    foreignMatrixPath[path] + tempMatrix[matrixIdentifier];
                continue;
            }
            tempMatrix[path] = findShortestPath(
                tempMatrix[path],
                tempMatrix[matrixIdentifier],
                foreignMatrixPath[path],
            );
        }
        resolve(tempMatrix);
    });
}

function send() {
    setTimeout(send, 5000);
    for (const directNeighbor of directNeighbors) {
        xmpp.send(
            `${directNeighbor.toLowerCase()}@localhost`,
            JSON.stringify({
                id,
                dv: JSON.stringify(matrix),
            }),
        );
    }
}

xmpp.on('online', _data => {
    console.log(
        `You are now online. You are now broadcasting to your neighbors, which are: ${directNeighbors}.`,
    );
});

xmpp.on('error', console.log);

xmpp.on('chat', async (from, message) => {
    if (!acceptingNewMessagesYet) return;
    let { id: foreignID, dv } = JSON.parse(message);
    dv = JSON.parse(dv);
    generateMatrix(dv, foreignID).then(newMatrix => {
        matrix = newMatrix;
        console.log(matrix);
    });
});
send();
setTimeout(() => (acceptingNewMessagesYet = true), 5000);

xmpp.connect({
    jid: `${id.toLowerCase()}@localhost`,
    password: 'password',
    host: 'localhost',
    port: 5222,
});
