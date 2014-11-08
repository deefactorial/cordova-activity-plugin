/*
 *
 * Copyright 2014 Dominique Legault
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var argscheck = require('cordova/argscheck'),
    exec = require('cordova/exec');

//module.exports = function(intent,jsonArray) {
//    exec(null, null, "OpenActivity", intent, jsonArray);
//};


module.exports = {
		startActivity : function(activity, callback) {
			// use node.js style error reporting (first argument)
	         cordova.exec(function(response){
	            callback(false, response);
	         }, function(err) {
	            callback(err);
	        }, "OpenActivity", activity, []);
		},
		sendErrorReport : function (args, callback) {
			// use node.js style error reporting (first argument)
	         cordova.exec(function(response){
	            callback(false, response);
	         }, function(err) {
	            callback(err);
	        }, "OpenActivity", "SendErrorReport", args);
		},
		NFCSettings : function (callback) {
			// use node.js style error reporting (first argument)
	         cordova.exec(function(response){
	            callback(false, response);
	         }, function(err) {
	            callback(err);
	        }, "OpenActivity", "NFCSettings", []);
		},
		getReplicationStatus : function(callback) {
	         // use node.js style error reporting (first argument)
	         cordova.exec(function(response){
	            callback(false, response);
	         }, function(err) {
	            callback(err);
	        }, "OpenActivity", "getReplicationStatus", []);
	    },
	    setReplicationChangeListener : function(callback) {
	         // use node.js style error reporting (first argument)
	         cordova.exec(function(response){
	            callback(false, response);
	         }, function(err) {
	            callback(err);
	        }, "OpenActivity", "setReplicationChangeListener", []);
	    },
	}
