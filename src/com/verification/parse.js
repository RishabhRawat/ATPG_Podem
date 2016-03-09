var all_nets = [];
var all_components = [];

function create_schema(inputFile) {
    var double_nets = {};
    var data = JSON.parse(inputFile);
    var m = Object.keys(data.modules);
    var module = data.modules[m];
    var port_list = module.ports;
    // var double_nets = {};
    //NET PARSING
    var net_list = module.netnames;
    for (var net_i in net_list) {
        for (var bit in net_list[net_i].bits) {
            if (all_nets.indexOf(net_list[net_i].bits[bit]) == -1)
                all_nets.push(net_list[net_i].bits[bit]);
        }
    }
    //PORT PARSING
    for (var port_i in port_list) {
        if (port_list[port_i].direction == 'input') {
            var c = {type: 'PI', inputs: [], outputs: []};
            for (var bit_i in port_list[port_i].bits) 
                c.outputs.push(port_list[port_i].bits[bit_i]);
            
        }
        else if (port_list[port_i].direction == 'output') {
            var c = {type: 'PO', inputs: [], outputs: []};
            for (var bit_i in port_list[port_i].bits) {
                c.inputs.push(port_list[port_i].bits[bit_i]);
                double_nets[port_list[port_i].bits[bit_i]] = 1;
            }
        }
        else
            print('invalid port direction: ' + port_list[port_i].direction);
        all_components.push(c);
    }

    //COMPONENT PARSING
    var cell_list = module.cells;
    for (var cell_i in cell_list) {
        var c = {type: '', inputs: [], outputs: []};
            switch(cell_list[cell_i].type){
                case '$or':
                    c.type = 'or_gate';
                    break;
                case '$and':
                    c.type = 'and_gate';
                    break;
                case '$not':
                    c.type = 'not_gate';
                    break;
                default:
                    print('UNRECOGNISED TYPE OF COMPONENT!! error likey!!');
                    c.type = 'other';
            }
        for (var port_i in cell_list[cell_i].connections) {
            if (cell_list[cell_i].port_directions[port_i] == 'input') {
                c.inputs = c.inputs.concat(cell_list[cell_i].connections[port_i]);
                for (var i = 0; i < cell_list[cell_i].connections[port_i].length; i++) {
                    if(cell_list[cell_i].connections[port_i][i] in double_nets)
                        double_nets[cell_list[cell_i].connections[port_i][i]] = double_nets[cell_list[cell_i].connections[port_i][i]]+1;
                    else
                        double_nets[cell_list[cell_i].connections[port_i][i]] = 1;
                }
            }
            else if (cell_list[cell_i].port_directions[port_i] == 'output') {
                c.outputs = c.outputs.concat(cell_list[cell_i].connections[port_i]);
            }
            else
                print('WRONG NET DIRECTION' + cell_i);
        }
        all_components.push(c);
    }

    var l = all_nets.length+2;
    //DEDUPLICATING NETS
    for (var k in double_nets) {
        if(double_nets[k] > 1){
            var temp = l;
            var c = {type: 'fanout_gate', inputs: [parseInt(k,10)], outputs: []};
            for(var comp in all_components){
                var index = all_components[comp].inputs.indexOf(parseInt(k));
                if (index != -1) {
                    all_components[comp].inputs[index] = l;
                    all_nets.push(l|0);
                    l++;
                }
            }
            for (; temp < l; temp++) {
                c.outputs.push(temp);
            }
            all_components.push(c);
        }
    }
}
