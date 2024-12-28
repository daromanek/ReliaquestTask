import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Button } from 'react-bootstrap';

const EmployeeTable = () => {
    const [employees, setEmployees] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showSearchModal, setShowSearchModal] = useState(false);
    const [highestSalary, setHighestSalary] = useState(null);
    const [topTenNames, setTopTenNames] = useState([]);
    const [newEmployee, setNewEmployee] = useState({ name: '', salary: '', age: '', title: '' });
    const [searchString, setSearchString] = useState('');
    const [searchResults, setSearchResults] = useState([]);

    useEffect(() => {
        fetchEmployees();
    }, []);

    const fetchEmployees = async () => {
        const response = await axios.get('http://localhost:8111/api/v1/employee');
        setEmployees(response.data);
    };

    const deleteEmployee = async (id) => {
        try {
            await axios.delete(`http://localhost:8111/api/v1/employee/${id}`);
            setEmployees(employees.filter(employee => employee.id !== id));
        } catch (error) {
            console.error("Error deleting employee:", error);
        }
    };

    const createEmployee = async () => {
        try {
            const response = await axios.post('http://localhost:8111/api/v1/employee', {
                name: newEmployee.name,
                salary: newEmployee.salary,
                age: newEmployee.age,
                title: newEmployee.title
            });
            setEmployees([...employees, response.data]);
            setShowCreateModal(false);
            setNewEmployee({ name: '', salary: '', age: '', title: '' });
        } catch (error) {
            console.error("Error creating employee:", error);
        }
    };

    const searchEmployees = async () => {
        const response = await axios.get(`http://localhost:8111/api/v1/employee/search/${searchString}`);
        setSearchResults(response.data);
        setShowSearchModal(true);
    };

    const getHighestSalary = async () => {
        const response = await axios.get('http://localhost:8111/api/v1/employee/highestSalary');
        setHighestSalary(response.data);
    };

    const getTopTenEmployees = async () => {
        const response = await axios.get('http://localhost:8111/api/v1/employee/topTenHighestEarningEmployeeNames');
        setTopTenNames(response.data);
    };

    return (
        <div className="container mt-5">
            <h2>Employee Management</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Salary</th>
                        <th>Age</th>
                        <th>Title</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {(searchResults.length > 0 ? searchResults : employees).map(employee => (
                        <tr key={employee.id}>
                            <td>{employee.employee_name}</td>
                            <td>{employee.employee_salary}</td>
                            <td>{employee.employee_age}</td>
                            <td>{employee.employee_title}</td>
                            <td>
                                <button className="btn btn-danger" onClick={() => deleteEmployee(employee.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button className="btn btn-primary" onClick={() => setShowCreateModal(true)}>Create Employee</button>
            <button className="btn btn-secondary" onClick={() => setShowSearchModal(true)}>Search Employee</button>
            <button className="btn btn-info" onClick={getHighestSalary}>Highest Salary</button>
            <button className="btn btn-warning" onClick={getTopTenEmployees}>Top 10 Highest Earning Employees</button>

            {/* Create Employee Modal */}
            <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Create Employee</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <input type="text" placeholder="Name" className="form-control" value={newEmployee.name} onChange={(e) => setNewEmployee({ ...newEmployee, name: e.target.value })} />
                    <input type="number" placeholder="Salary" className="form-control mt-2" value={newEmployee.salary} onChange={(e) => setNewEmployee({ ...newEmployee, salary: e.target.value })} />
                    <input type="number" placeholder="Age" className="form-control mt-2" value={newEmployee.age} onChange={(e) => setNewEmployee({ ...newEmployee, age: e.target.value })} />
                    <input type="text" placeholder="Title" className="form-control mt-2" value={newEmployee.title} onChange={(e) => setNewEmployee({ ...newEmployee, title: e.target.value })} />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowCreateModal(false)}>Close</Button>
                    <Button variant="primary" onClick={createEmployee}>Create</Button>
                </Modal.Footer>
            </Modal>

            {/* Search Employee Modal */}
            <Modal show={showSearchModal} onHide={() => setShowSearchModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Search Employee</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <input type="text" placeholder="Search String" className="form-control" onChange={(e) => setSearchString(e.target.value)} />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowSearchModal(false)}>Close</Button>
                    <Button variant="primary" onClick={searchEmployees}>Search</Button>
                </Modal.Footer>
            </Modal>

            {/* Search Results Modal */}
            <Modal show={searchResults.length > 0} onHide={() => setSearchResults([])}>
                <Modal.Header closeButton>
                    <Modal.Title>Search Results</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <ul>
                        {searchResults.map((employee, index) => (
                            <li key={index}>{employee.employee_name}</li>
                        ))}
                    </ul>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setSearchResults([])}>Close</Button>
                </Modal.Footer>
            </Modal>

            {/* Highest Salary Modal */}
            <Modal show={highestSalary !== null} onHide={() => setHighestSalary(null)}>
                <Modal.Header closeButton>
                    <Modal.Title>Highest Salary</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>The highest salary is: {highestSalary}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setHighestSalary(null)}>Close</Button>
                </Modal.Footer>
            </Modal>

            {/* Top 10 Employees Modal */}
            <Modal show={topTenNames.length > 0} onHide={() => setTopTenNames([])}>
                <Modal.Header closeButton>
                    <Modal.Title>Top 10 Highest Earning Employees</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <ul>
                        {topTenNames.map((name, index) => (
                            <li key={index}>{name}</li>
                        ))}
                    </ul>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setTopTenNames([])}>Close</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default EmployeeTable;