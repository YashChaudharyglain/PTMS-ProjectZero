package com.ptms.app.controller;

import com.ptms.app.dao.ClientDAO;
import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.dao.TicketManagementDAO;
import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.dao.UserDAO;

import com.ptms.app.dao.impl.ClientDAOImpl;
import com.ptms.app.dao.impl.ProjectDAOImpl;
import com.ptms.app.dao.impl.ProjectMemberDAOImpl;
import com.ptms.app.dao.impl.TicketManagementDAOImpl;
import com.ptms.app.dao.impl.TicketTrackingDAOImpl;
import com.ptms.app.dao.impl.UserDAOImpl;

import com.ptms.app.model.Client;
import com.ptms.app.model.Project;
import com.ptms.app.model.ProjectMember;
import com.ptms.app.model.TicketManagement;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;

import com.ptms.app.service.ClientService;
import com.ptms.app.service.ProjectMemberService;
import com.ptms.app.service.ProjectService;
import com.ptms.app.service.TicketManagementService;
import com.ptms.app.service.TicketTrackingService;
import com.ptms.app.service.UserService;

import com.ptms.app.service.impl.ClientServiceImpl;
import com.ptms.app.service.impl.ProjectMemberServiceImpl;
import com.ptms.app.service.impl.ProjectServiceImpl;
import com.ptms.app.service.impl.TicketManagementServiceImpl;
import com.ptms.app.service.impl.TicketTrackingServiceImpl;
import com.ptms.app.service.impl.UserServiceImpl;

import com.ptms.app.util.LoggerUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class MainController {

    private static final Logger logger = LoggerUtil.getLogger(MainController.class);
    private static final Scanner scanner = new Scanner(System.in);

    private static final UserDAO userDAO = new UserDAOImpl();
    private static final ClientDAO clientDAO = new ClientDAOImpl();
    private static final ProjectDAO projectDAO = new ProjectDAOImpl();
    private static final ProjectMemberDAO projectMemberDAO = new ProjectMemberDAOImpl();
    private static final TicketManagementDAO ticketManagementDAO = new TicketManagementDAOImpl();
    private static final TicketTrackingDAO ticketTrackingDAO = new TicketTrackingDAOImpl();

    private static final UserService userService = new UserServiceImpl(userDAO);
    private static final ClientService clientService = new ClientServiceImpl(clientDAO);
    private static final ProjectService projectService = new ProjectServiceImpl(projectDAO);
    private static final ProjectMemberService projectMemberService = new ProjectMemberServiceImpl(projectMemberDAO);
    private static final TicketManagementService ticketManagementService = new TicketManagementServiceImpl(ticketManagementDAO);
    private static final TicketTrackingService ticketTrackingService = new TicketTrackingServiceImpl(ticketTrackingDAO);

    private static final UserController userController = new UserController(userService);
    private static final ClientController clientController = new ClientController(clientService);
    private static final ProjectController projectController = new ProjectController(projectService);
    private static final ProjectMemberController projectMemberController = new ProjectMemberController(projectMemberService);
    private static final TicketManagementController ticketManagementController = new TicketManagementController(ticketManagementService);
    private static final TicketTrackingController ticketTrackingController = new TicketTrackingController(ticketTrackingService);

    public static void main(String[] args) {
        logger.info("PTMS Started Successfully");

        System.out.println("==========================================");
        System.out.println("        PROJECT TRACKING MANAGEMENT");
        System.out.println("                 SYSTEM");
        System.out.println("==========================================");

        while (true) {
            System.out.println();
            System.out.println("------------- MAIN MENU ----------------");
            System.out.println("1. Login");
            System.out.println("2. Register New User");
            System.out.println("3. Exit");
            System.out.println("----------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) {
                User user = handleLogin();
                if (user != null) {
                    showDashboard(user);
                }
            } else if (choice == 2) {
                handleRegistration();
            } else if (choice == 3) {
                System.out.println("PTMS Exited Successfully.");
                break;
            } else {
                System.out.println("Invalid option. Please select 1-3.");
            }
        }

        scanner.close();
    }

    private static User handleLogin() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("                USER LOGIN");
        System.out.println("==========================================");

        String username = readRequiredString("Enter Username: ");
        String password = readRequiredString("Enter Password: ");

        try {
            User user = userController.login(username, password);

            if (user == null) {
                System.out.println("[ERROR] Invalid username or password.");
                return null;
            }

            System.out.println("[SUCCESS] Login successful!");
            System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Role: " + user.getRoleName());

            return user;
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed: " + safeMessage(e));
            return null;
        }
    }

    private static void handleRegistration() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("             REGISTER USER");
        System.out.println("==========================================");

        try {
            User user = new User();

            user.setFirstName(readRequiredString("Enter First Name: "));
            user.setLastName(readRequiredString("Enter Last Name: "));
            user.setUsername(readRequiredString("Enter Username: "));
            user.setEmail(readRequiredString("Enter Email: "));
            user.setPassword(readRequiredString("Enter Password: "));
            user.setRoleName(selectUserRole());
            user.setDateOfBirth(readDate("Enter Date of Birth (YYYY-MM-DD): "));
            user.setMobileNumber(readRequiredString("Enter Mobile Number: "));
            user.setGender(selectGender());

            userController.createUser(user);

            System.out.println("[SUCCESS] Registration completed.");
            System.out.println("You can now login.");
        } catch (Exception e) {
            System.out.println("[ERROR] Registration failed: " + safeMessage(e));
        }
    }

    private static String selectUserRole() {
        while (true) {
            System.out.println();
            System.out.println("Select User Role:");
            System.out.println("1. ADMIN");
            System.out.println("2. MANAGER");
            System.out.println("3. PROJECT_MANAGER");
            System.out.println("4. TEAM_LEAD");

            int choice = readInt("Select Role: ");

            switch (choice) {
                case 1: return "ADMIN";
                case 2: return "MANAGER";
                case 3: return "PROJECT_MANAGER";
                case 4: return "TEAM_LEAD";
                default: System.out.println("Invalid role. Please select 1-4.");
            }
        }
    }

    private static String selectGender() {
        while (true) {
            System.out.println();
            System.out.println("Select Gender:");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.println("3. Other");

            int choice = readInt("Select Gender: ");

            switch (choice) {
                case 1: return "Male";
                case 2: return "Female";
                case 3: return "Other";
                default: System.out.println("Invalid option. Please select 1-3.");
            }
        }
    }

    private static void showDashboard(User user) {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("              PTMS DASHBOARD");
            System.out.println("==========================================");
            System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Role: " + user.getRoleName());
            System.out.println();

            System.out.println("1. View My Profile");

            if (isAdminOrManager(user)) {
                System.out.println("2. Client Management");
                System.out.println("3. Project Management");
                System.out.println("4. Project Member Management");
                System.out.println("5. Ticket Management");
                System.out.println("6. Ticket Tracking");
            } else if (isTeamLead(user)) {
                System.out.println("2. Project Management");
                System.out.println("3. Project Member Management");
                System.out.println("4. Ticket Management");
                System.out.println("5. Ticket Tracking");
            } else {
                System.out.println("2. Project Management");
                System.out.println("3. Ticket Management");
                System.out.println("4. Ticket Tracking");
            }

            System.out.println("0. Logout");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (isAdminOrManager(user)) {
                if (choice == 1) showMyProfile(user);
                else if (choice == 2) clientManagement();
                else if (choice == 3) projectManagement();
                else if (choice == 4) projectMemberManagement();
                else if (choice == 5) ticketManagement();
                else if (choice == 6) ticketTracking();
                else if (choice == 0) {
                    System.out.println("Logged out successfully.");
                    return;
                } else System.out.println("Invalid option.");
            } else if (isTeamLead(user)) {
                if (choice == 1) showMyProfile(user);
                else if (choice == 2) projectManagement();
                else if (choice == 3) projectMemberManagement();
                else if (choice == 4) ticketManagement();
                else if (choice == 5) ticketTracking();
                else if (choice == 0) {
                    System.out.println("Logged out successfully.");
                    return;
                } else System.out.println("Invalid option.");
            } else {
                if (choice == 1) showMyProfile(user);
                else if (choice == 2) projectManagement();
                else if (choice == 3) ticketManagement();
                else if (choice == 4) ticketTracking();
                else if (choice == 0) {
                    System.out.println("Logged out successfully.");
                    return;
                } else System.out.println("Invalid option.");
            }
        }
    }

    private static void showMyProfile(User user) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("               MY PROFILE");
        System.out.println("==========================================");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRoleName());
        System.out.println("Date of Birth: " + user.getDateOfBirth());
        System.out.println("Mobile: " + user.getMobileNumber());
        System.out.println("Gender: " + user.getGender());
    }

    private static void clientManagement() {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("            CLIENT MANAGEMENT");
            System.out.println("==========================================");
            System.out.println("1. Create Client");
            System.out.println("2. Get Client By ID");
            System.out.println("3. Get All Clients");
            System.out.println("4. Search Clients");
            System.out.println("5. Update Client");
            System.out.println("6. Delete Client");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) createClient();
            else if (choice == 2) getClientById();
            else if (choice == 3) getAllClients();
            else if (choice == 4) searchClients();
            else if (choice == 5) updateClient();
            else if (choice == 6) deleteClient();
            else if (choice == 0) return;
            else System.out.println("Invalid option.");
        }
    }

    private static void createClient() {
        try {
            Client client = new Client();
            client.setName(readRequiredString("Enter Client Name: "));
            client.setEmail(readRequiredString("Enter Email: "));
            client.setPhone(readRequiredString("Enter Phone: "));
            client.setCompanyName(readRequiredString("Enter Company Name: "));

            clientController.createClient(client);
            System.out.println("[SUCCESS] Client created successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getClientById() {
        int id = selectClientId();
        if (id == -1) return;

        try {
            Client client = clientController.getClientById(id);
            if (client == null) {
                System.out.println("Client not found.");
                return;
            }
            displayClient(client);
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getAllClients() {
        try {
            List<Client> clients = clientController.getAllClients();

            if (clients.isEmpty()) {
                System.out.println("No clients found.");
                return;
            }

            System.out.println("---------- ALL CLIENTS ----------");
            for (Client client : clients) {
                displayClient(client);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void searchClients() {
        String keyword = readRequiredString("Enter Search Keyword: ");

        try {
            List<Client> clients = clientController.searchClients(keyword);

            if (clients.isEmpty()) {
                System.out.println("No clients found.");
                return;
            }

            for (Client client : clients) {
                displayClient(client);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void updateClient() {
        int id = selectClientId();
        if (id == -1) return;

        try {
            Client client = clientController.getClientById(id);

            if (client == null) {
                System.out.println("Client not found.");
                return;
            }

            client.setName(readRequiredString("Enter Client Name: "));
            client.setEmail(readRequiredString("Enter Email: "));
            client.setPhone(readRequiredString("Enter Phone: "));
            client.setCompanyName(readRequiredString("Enter Company Name: "));

            clientController.updateClient(client);
            System.out.println("[SUCCESS] Client updated successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void deleteClient() {
        int id = selectClientId();
        if (id == -1) return;

        try {
            clientController.deleteClient(id);
            System.out.println("[SUCCESS] Client deleted successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void displayClient(Client client) {
        System.out.println("ID: " + client.getId());
        System.out.println("Name: " + client.getName());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Phone: " + client.getPhone());
        System.out.println("Company: " + client.getCompanyName());
    }

    private static void projectManagement() {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("            PROJECT MANAGEMENT");
            System.out.println("==========================================");
            System.out.println("1. Create Project");
            System.out.println("2. Get Project By ID");
            System.out.println("3. Get All Projects");
            System.out.println("4. Search Projects");
            System.out.println("5. Update Project");
            System.out.println("6. Delete Project");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) createProject();
            else if (choice == 2) getProjectById();
            else if (choice == 3) getAllProjects();
            else if (choice == 4) searchProjects();
            else if (choice == 5) updateProject();
            else if (choice == 6) deleteProject();
            else if (choice == 0) return;
            else System.out.println("Invalid option.");
        }
    }

    private static void createProject() {
        System.out.println();
        System.out.println("---------- CREATE PROJECT ----------");

        try {
            if (userController.getAllUsers().isEmpty()) {
                System.out.println("No users available. Register users first.");
                return;
            }

            if (clientController.getAllClients().isEmpty()) {
                System.out.println("No clients available. Create a client first.");
                return;
            }

            Project project = new Project();

            project.setName(readRequiredString("Enter Project Name: "));
            project.setRequirements(readRequiredString("Enter Requirements: "));

            int managerId = selectUserForRole("SELECT MANAGER", "MANAGER", "PROJECT_MANAGER", "ADMIN");
            if (managerId == -1) return;

            int teamLeadId = selectUserForRole("SELECT TEAM LEAD", "TEAM_LEAD");
            if (teamLeadId == -1) return;

            int clientId = selectClientId();
            if (clientId == -1) return;

            project.setManagerId(managerId);
            project.setTeamLeadId(teamLeadId);
            project.setClientId(clientId);

            project.setDomain(readRequiredString("Enter Domain: "));
            project.setCost(readBigDecimal("Enter Cost: "));
            project.setStartDate(readDate("Enter Start Date (YYYY-MM-DD): "));
            project.setDeadline(readDate("Enter Deadline (YYYY-MM-DD): "));
            project.setPriority(selectPriority());
            project.setStatus(selectProjectStatus());

            projectController.createProject(project);

            System.out.println("[SUCCESS] Project created successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Project creation failed: " + safeMessage(e));
        }
    }

    private static void getProjectById() {
        int id = selectProjectId();
        if (id == -1) return;

        try {
            Project project = projectController.getProjectById(id);

            if (project == null) {
                System.out.println("Project not found.");
                return;
            }

            displayProject(project);
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getAllProjects() {
        try {
            List<Project> projects = projectController.getAllProjects();

            if (projects.isEmpty()) {
                System.out.println("No projects found.");
                return;
            }

            System.out.println("---------- ALL PROJECTS ----------");

            for (Project project : projects) {
                displayProject(project);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void searchProjects() {
        String keyword = readRequiredString("Enter Search Keyword: ");

        try {
            List<Project> projects = projectController.searchProjects(keyword);

            if (projects.isEmpty()) {
                System.out.println("No projects found.");
                return;
            }

            for (Project project : projects) {
                displayProject(project);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void updateProject() {
        int id = selectProjectId();
        if (id == -1) return;

        try {
            Project project = projectController.getProjectById(id);

            if (project == null) {
                System.out.println("Project not found.");
                return;
            }

            project.setName(readRequiredString("Enter Project Name: "));
            project.setRequirements(readRequiredString("Enter Requirements: "));

            int managerId = selectUserForRole("SELECT MANAGER", "MANAGER", "PROJECT_MANAGER", "ADMIN");
            if (managerId == -1) return;

            int teamLeadId = selectUserForRole("SELECT TEAM LEAD", "TEAM_LEAD");
            if (teamLeadId == -1) return;

            int clientId = selectClientId();
            if (clientId == -1) return;

            project.setManagerId(managerId);
            project.setTeamLeadId(teamLeadId);
            project.setClientId(clientId);
            project.setDomain(readRequiredString("Enter Domain: "));
            project.setCost(readBigDecimal("Enter Cost: "));
            project.setStartDate(readDate("Enter Start Date (YYYY-MM-DD): "));
            project.setDeadline(readDate("Enter Deadline (YYYY-MM-DD): "));
            project.setPriority(selectPriority());
            project.setStatus(selectProjectStatus());

            projectController.updateProject(project);

            System.out.println("[SUCCESS] Project updated successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Project update failed: " + safeMessage(e));
        }
    }

    private static void deleteProject() {
        int id = selectProjectId();
        if (id == -1) return;

        try {
            projectController.deleteProject(id);
            System.out.println("[SUCCESS] Project deleted successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void displayProject(Project project) {
        System.out.println("ID: " + project.getId());
        System.out.println("Name: " + project.getName());
        System.out.println("Requirements: " + project.getRequirements());
        System.out.println("Manager ID: " + project.getManagerId());
        System.out.println("Team Lead ID: " + project.getTeamLeadId());
        System.out.println("Client ID: " + project.getClientId());
        System.out.println("Domain: " + project.getDomain());
        System.out.println("Cost: " + project.getCost());
        System.out.println("Start Date: " + project.getStartDate());
        System.out.println("Deadline: " + project.getDeadline());
        System.out.println("Priority: " + project.getPriority());
        System.out.println("Status: " + project.getStatus());
    }

    private static void projectMemberManagement() {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("        PROJECT MEMBER MANAGEMENT");
            System.out.println("==========================================");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. Check Member");
            System.out.println("4. Find Members By Project");
            System.out.println("5. Find Projects By User");
            System.out.println("6. Update Member Role");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) addProjectMember();
            else if (choice == 2) removeProjectMember();
            else if (choice == 3) checkProjectMember();
            else if (choice == 4) findMembersByProject();
            else if (choice == 5) findProjectsByUser();
            else if (choice == 6) updateMemberRole();
            else if (choice == 0) return;
            else System.out.println("Invalid option.");
        }
    }

    private static void addProjectMember() {
        try {
            int projectId = selectProjectId();
            if (projectId == -1) return;

            int userId = selectUserId();
            if (userId == -1) return;

            if (projectMemberController.isMember(projectId, userId)) {
                System.out.println("[ERROR] User is already a member of this project.");
                return;
            }

            ProjectMember member = new ProjectMember();
            member.setProjectId(projectId);
            member.setUserId(userId);
            member.setJoinedAt(readDate("Enter Joined Date (YYYY-MM-DD): "));
            member.setRoleInProject(selectProjectMemberRole());

            boolean result = projectMemberController.addMember(member);

            System.out.println(result
                    ? "[SUCCESS] Member added successfully."
                    : "[ERROR] Member could not be added.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void removeProjectMember() {
        int projectId = selectProjectId();
        if (projectId == -1) return;

        int userId = selectUserId();
        if (userId == -1) return;

        try {
            boolean result = projectMemberController.removeMember(projectId, userId);

            System.out.println(result
                    ? "[SUCCESS] Member removed successfully."
                    : "[ERROR] Member not found.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void checkProjectMember() {
        int projectId = selectProjectId();
        if (projectId == -1) return;

        int userId = selectUserId();
        if (userId == -1) return;

        try {
            boolean result = projectMemberController.isMember(projectId, userId);

            System.out.println(result
                    ? "User is a member of this project."
                    : "User is not a member of this project.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void findMembersByProject() {
        int projectId = selectProjectId();
        if (projectId == -1) return;

        try {
            List<ProjectMember> members = projectMemberController.findByProject(projectId);

            if (members.isEmpty()) {
                System.out.println("No members found.");
                return;
            }

            for (ProjectMember member : members) {
                displayProjectMember(member);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void findProjectsByUser() {
        int userId = selectUserId();
        if (userId == -1) return;

        try {
            List<ProjectMember> members = projectMemberController.findByUser(userId);

            if (members.isEmpty()) {
                System.out.println("No projects found for this user.");
                return;
            }

            for (ProjectMember member : members) {
                displayProjectMember(member);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void updateMemberRole() {
        int projectId = selectProjectId();
        if (projectId == -1) return;

        int userId = selectUserId();
        if (userId == -1) return;

        String role = selectProjectMemberRole();

        try {
            boolean result = projectMemberController.updateRole(projectId, userId, role);

            System.out.println(result
                    ? "[SUCCESS] Member role updated."
                    : "[ERROR] Member not found.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static String selectProjectMemberRole() {
        while (true) {
            System.out.println();
            System.out.println("Select Project Member Role:");
            System.out.println("1. PROJECT_MANAGER");
            System.out.println("2. TEAM_LEAD");
            System.out.println("3. DEVELOPER");
            System.out.println("4. TESTER");

            int choice = readInt("Select Role: ");

            switch (choice) {
                case 1: return "PROJECT_MANAGER";
                case 2: return "TEAM_LEAD";
                case 3: return "DEVELOPER";
                case 4: return "TESTER";
                default: System.out.println("Invalid role. Please select 1-4.");
            }
        }
    }

    private static void displayProjectMember(ProjectMember member) {
        System.out.println("Project ID: " + member.getProjectId());
        System.out.println("User ID: " + member.getUserId());
        System.out.println("Joined At: " + member.getJoinedAt());
        System.out.println("Role: " + member.getRoleInProject());
    }

    private static void ticketManagement() {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("             TICKET MANAGEMENT");
            System.out.println("==========================================");
            System.out.println("1. Create Ticket");
            System.out.println("2. Get Ticket By ID");
            System.out.println("3. Get All Tickets");
            System.out.println("4. Get Tickets By Project");
            System.out.println("5. Get Tickets By User");
            System.out.println("6. Search Tickets");
            System.out.println("7. Update Ticket");
            System.out.println("8. Delete Ticket");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) createTicket();
            else if (choice == 2) getTicketById();
            else if (choice == 3) getAllTickets();
            else if (choice == 4) getTicketsByProject();
            else if (choice == 5) getTicketsByUser();
            else if (choice == 6) searchTickets();
            else if (choice == 7) updateTicket();
            else if (choice == 8) deleteTicket();
            else if (choice == 0) return;
            else System.out.println("Invalid option.");
        }
    }

    private static void createTicket() {
        try {
            int projectId = selectProjectId();
            if (projectId == -1) return;

            int assignedTo = selectUserId();
            if (assignedTo == -1) return;

            TicketManagement ticket = new TicketManagement();
            ticket.setProjectId(projectId);
            ticket.setTitle(readRequiredString("Enter Ticket Title: "));
            ticket.setDescription(readRequiredString("Enter Description: "));
            ticket.setPriority(selectPriority());
            ticket.setDeadline(readDate("Enter Deadline (YYYY-MM-DD): "));
            ticket.setAssignedTo(assignedTo);
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setStatus(selectTicketStatus());

            ticketManagementController.createTicket(ticket);

            System.out.println("[SUCCESS] Ticket created successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Ticket creation failed: " + safeMessage(e));
        }
    }

    private static void getTicketById() {
        int id = selectTicketId();
        if (id == -1) return;

        try {
            TicketManagement ticket = ticketManagementController.getTicketById(id);

            if (ticket == null) {
                System.out.println("Ticket not found.");
                return;
            }

            displayTicket(ticket);
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getAllTickets() {
        try {
            List<TicketManagement> tickets = ticketManagementController.getAllTickets();

            if (tickets.isEmpty()) {
                System.out.println("No tickets found.");
                return;
            }

            for (TicketManagement ticket : tickets) {
                displayTicket(ticket);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getTicketsByProject() {
        int projectId = selectProjectId();
        if (projectId == -1) return;

        try {
            List<TicketManagement> tickets =
                    ticketManagementController.getTicketsByProject(projectId);

            if (tickets.isEmpty()) {
                System.out.println("No tickets found.");
                return;
            }

            for (TicketManagement ticket : tickets) {
                displayTicket(ticket);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void getTicketsByUser() {
        int userId = selectUserId();
        if (userId == -1) return;

        try {
            List<TicketManagement> tickets =
                    ticketManagementController.getTicketsByUser(userId);

            if (tickets.isEmpty()) {
                System.out.println("No tickets found.");
                return;
            }

            for (TicketManagement ticket : tickets) {
                displayTicket(ticket);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void searchTickets() {
        String keyword = readRequiredString("Enter Search Keyword: ");

        try {
            List<TicketManagement> tickets =
                    ticketManagementController.searchTickets(keyword);

            if (tickets.isEmpty()) {
                System.out.println("No tickets found.");
                return;
            }

            for (TicketManagement ticket : tickets) {
                displayTicket(ticket);
                System.out.println("--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void updateTicket() {
        int id = selectTicketId();
        if (id == -1) return;

        try {
            TicketManagement ticket = ticketManagementController.getTicketById(id);

            if (ticket == null) {
                System.out.println("Ticket not found.");
                return;
            }

            int projectId = selectProjectId();
            if (projectId == -1) return;

            int assignedTo = selectUserId();
            if (assignedTo == -1) return;

            ticket.setProjectId(projectId);
            ticket.setTitle(readRequiredString("Enter Ticket Title: "));
            ticket.setDescription(readRequiredString("Enter Description: "));
            ticket.setPriority(selectPriority());
            ticket.setDeadline(readDate("Enter Deadline (YYYY-MM-DD): "));
            ticket.setAssignedTo(assignedTo);
            ticket.setStatus(selectTicketStatus());

            ticketManagementController.updateTicket(ticket);

            System.out.println("[SUCCESS] Ticket updated successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Ticket update failed: " + safeMessage(e));
        }
    }

    private static void deleteTicket() {
        int id = selectTicketId();
        if (id == -1) return;

        try {
            ticketManagementController.deleteTicket(id);
            System.out.println("[SUCCESS] Ticket deleted successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void displayTicket(TicketManagement ticket) {
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Project ID: " + ticket.getProjectId());
        System.out.println("Title: " + ticket.getTitle());
        System.out.println("Description: " + ticket.getDescription());
        System.out.println("Priority: " + ticket.getPriority());
        System.out.println("Deadline: " + ticket.getDeadline());
        System.out.println("Assigned To: " + ticket.getAssignedTo());
        System.out.println("Created At: " + ticket.getCreatedAt());
        System.out.println("Status: " + ticket.getStatus());
    }

    private static void ticketTracking() {
        while (true) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("              TICKET TRACKING");
            System.out.println("==========================================");
            System.out.println("1. Create Tracking");
            System.out.println("2. Get Tracking By Ticket");
            System.out.println("3. Update Tracking");
            System.out.println("4. Delete Tracking");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Select an option: ");

            if (choice == 1) createTracking();
            else if (choice == 2) getTracking();
            else if (choice == 3) updateTracking();
            else if (choice == 4) deleteTracking();
            else if (choice == 0) return;
            else System.out.println("Invalid option.");
        }
    }

    private static void createTracking() {
        try {
            int ticketId = selectTicketId();
            if (ticketId == -1) return;

            TicketTracking existing =
                    ticketTrackingController.getTrackingByTicketId(ticketId);

            if (existing != null) {
                System.out.println("[ERROR] Tracking already exists for this ticket.");
                return;
            }

            TicketTracking tracking = new TicketTracking();
            tracking.setTicketId(ticketId);
            tracking.setStatus(selectTrackingStatus());
            tracking.setProgress(readProgress());
            tracking.setComment(readRequiredString("Enter Comment: "));

            int updatedBy = selectUserId();
            if (updatedBy == -1) return;

            tracking.setUpdatedBy(updatedBy);
            tracking.setUpdatedAt(LocalDateTime.now());

            ticketTrackingController.createTracking(tracking);

            System.out.println("[SUCCESS] Ticket tracking created successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Tracking creation failed: " + safeMessage(e));
        }
    }

    private static void getTracking() {
        int ticketId = selectTicketId();
        if (ticketId == -1) return;

        try {
            TicketTracking tracking =
                    ticketTrackingController.getTrackingByTicketId(ticketId);

            if (tracking == null) {
                System.out.println("Tracking information not found.");
                return;
            }

            displayTracking(tracking);
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void updateTracking() {
        int ticketId = selectTicketId();
        if (ticketId == -1) return;

        try {
            TicketTracking tracking =
                    ticketTrackingController.getTrackingByTicketId(ticketId);

            if (tracking == null) {
                System.out.println("Tracking information not found.");
                return;
            }

            tracking.setStatus(selectTrackingStatus());
            tracking.setProgress(readProgress());
            tracking.setComment(readRequiredString("Enter Comment: "));

            int updatedBy = selectUserId();
            if (updatedBy == -1) return;

            tracking.setUpdatedBy(updatedBy);
            tracking.setUpdatedAt(LocalDateTime.now());

            ticketTrackingController.updateTracking(tracking);

            System.out.println("[SUCCESS] Ticket tracking updated successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Tracking update failed: " + safeMessage(e));
        }
    }

    private static void deleteTracking() {
        int ticketId = selectTicketId();
        if (ticketId == -1) return;

        try {
            ticketTrackingController.deleteTracking(ticketId);
            System.out.println("[SUCCESS] Ticket tracking deleted successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + safeMessage(e));
        }
    }

    private static void displayTracking(TicketTracking tracking) {
        System.out.println("Tracking ID: " + tracking.getId());
        System.out.println("Ticket ID: " + tracking.getTicketId());
        System.out.println("Status: " + tracking.getStatus());
        System.out.println("Progress: " + tracking.getProgress() + "%");
        System.out.println("Comment: " + tracking.getComment());
        System.out.println("Updated By: " + tracking.getUpdatedBy());
        System.out.println("Updated At: " + tracking.getUpdatedAt());
    }

    private static int selectUserId() {
        try {
            List<User> users = userController.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users available.");
                return -1;
            }

            System.out.println();
            System.out.println("---------- AVAILABLE USERS ----------");

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.println(
                        (i + 1) + ". ID: " + user.getId() +
                                " | " + user.getFirstName() + " " + user.getLastName() +
                                " | Role: " + user.getRoleName()
                );
            }

            int choice = readInt("Select User: ");

            if (choice < 1 || choice > users.size()) {
                System.out.println("Invalid user selection.");
                return -1;
            }

            return users.get(choice - 1).getId();
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to load users: " + safeMessage(e));
            return -1;
        }
    }

    private static int selectUserForRole(String title, String... allowedRoles) {
        try {
            List<User> users = userController.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users available.");
                return -1;
            }

            System.out.println();
            System.out.println("---------- " + title + " ----------");

            int displayed = 0;

            for (User user : users) {
                if (hasAllowedRole(user.getRoleName(), allowedRoles)) {
                    displayed++;
                    System.out.println(
                            displayed + ". ID: " + user.getId() +
                                    " | " + user.getFirstName() + " " + user.getLastName() +
                                    " | Role: " + user.getRoleName()
                    );
                }
            }

            if (displayed == 0) {
                System.out.println(
                        "No suitable user found."
                );
                System.out.println(
                        "Create a user with one of these roles:"
                );

                for (String role : allowedRoles) {
                    System.out.println("- " + role);
                }

                return -1;
            }

            int choice = readInt("Select User: ");

            if (choice < 1 || choice > displayed) {
                System.out.println("Invalid user selection.");
                return -1;
            }

            int current = 0;

            for (User user : users) {
                if (hasAllowedRole(user.getRoleName(), allowedRoles)) {
                    current++;

                    if (current == choice) {
                        return user.getId();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to load users: " + safeMessage(e));
        }

        return -1;
    }

    private static boolean hasAllowedRole(String actualRole, String... allowedRoles) {
        if (actualRole == null) return false;

        for (String role : allowedRoles) {
            if (role.equalsIgnoreCase(actualRole)) {
                return true;
            }
        }

        return false;
    }

    private static int selectClientId() {
        try {
            List<Client> clients = clientController.getAllClients();

            if (clients.isEmpty()) {
                System.out.println("No clients available.");
                System.out.println("Create a client first.");
                return -1;
            }

            System.out.println();
            System.out.println("---------- AVAILABLE CLIENTS ----------");

            for (int i = 0; i < clients.size(); i++) {
                Client client = clients.get(i);
                System.out.println(
                        (i + 1) + ". ID: " + client.getId() +
                                " | " + client.getName() +
                                " | Company: " + client.getCompanyName()
                );
            }

            int choice = readInt("Select Client: ");

            if (choice < 1 || choice > clients.size()) {
                System.out.println("Invalid client selection.");
                return -1;
            }

            return clients.get(choice - 1).getId();
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to load clients: " + safeMessage(e));
            return -1;
        }
    }

    private static int selectProjectId() {
        try {
            List<Project> projects = projectController.getAllProjects();

            if (projects.isEmpty()) {
                System.out.println("No projects available.");
                System.out.println("Create a project first.");
                return -1;
            }

            System.out.println();
            System.out.println("---------- AVAILABLE PROJECTS ----------");

            for (int i = 0; i < projects.size(); i++) {
                Project project = projects.get(i);
                System.out.println(
                        (i + 1) + ". ID: " + project.getId() +
                                " | " + project.getName() +
                                " | Status: " + project.getStatus()
                );
            }

            int choice = readInt("Select Project: ");

            if (choice < 1 || choice > projects.size()) {
                System.out.println("Invalid project selection.");
                return -1;
            }

            return projects.get(choice - 1).getId();
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to load projects: " + safeMessage(e));
            return -1;
        }
    }

    private static int selectTicketId() {
        try {
            List<TicketManagement> tickets =
                    ticketManagementController.getAllTickets();

            if (tickets.isEmpty()) {
                System.out.println("No tickets available.");
                System.out.println("Create a ticket first.");
                return -1;
            }

            System.out.println();
            System.out.println("---------- AVAILABLE TICKETS ----------");

            for (int i = 0; i < tickets.size(); i++) {
                TicketManagement ticket = tickets.get(i);
                System.out.println(
                        (i + 1) + ". ID: " + ticket.getId() +
                                " | " + ticket.getTitle() +
                                " | Status: " + ticket.getStatus()
                );
            }

            int choice = readInt("Select Ticket: ");

            if (choice < 1 || choice > tickets.size()) {
                System.out.println("Invalid ticket selection.");
                return -1;
            }

            return tickets.get(choice - 1).getId();
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to load tickets: " + safeMessage(e));
            return -1;
        }
    }

    private static String selectPriority() {
        while (true) {
            System.out.println();
            System.out.println("Select Priority:");
            System.out.println("1. LOW");
            System.out.println("2. MEDIUM");
            System.out.println("3. HIGH");
            System.out.println("4. CRITICAL");

            int choice = readInt("Select Priority: ");

            switch (choice) {
                case 1: return "LOW";
                case 2: return "MEDIUM";
                case 3: return "HIGH";
                case 4: return "CRITICAL";
                default: System.out.println("Invalid priority. Please select 1-4.");
            }
        }
    }

    private static String selectProjectStatus() {
        while (true) {
            System.out.println();
            System.out.println("Select Project Status:");
            System.out.println("1. PLANNED");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. COMPLETED");
            System.out.println("4. ON_HOLD");
            System.out.println("5. CANCELLED");

            int choice = readInt("Select Status: ");

            switch (choice) {
                case 1: return "PLANNED";
                case 2: return "IN_PROGRESS";
                case 3: return "COMPLETED";
                case 4: return "ON_HOLD";
                case 5: return "CANCELLED";
                default: System.out.println("Invalid status. Please select 1-5.");
            }
        }
    }

    private static String selectTicketStatus() {
        while (true) {
            System.out.println();
            System.out.println("Select Ticket Status:");
            System.out.println("1. OPEN");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. RESOLVED");
            System.out.println("4. CLOSED");

            int choice = readInt("Select Status: ");

            switch (choice) {
                case 1: return "OPEN";
                case 2: return "IN_PROGRESS";
                case 3: return "RESOLVED";
                case 4: return "CLOSED";
                default: System.out.println("Invalid status. Please select 1-4.");
            }
        }
    }

    private static String selectTrackingStatus() {
        while (true) {
            System.out.println();
            System.out.println("Select Tracking Status:");
            System.out.println("1. OPEN");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. RESOLVED");
            System.out.println("4. CLOSED");

            int choice = readInt("Select Status: ");

            switch (choice) {
                case 1: return "OPEN";
                case 2: return "IN_PROGRESS";
                case 3: return "RESOLVED";
                case 4: return "CLOSED";
                default: System.out.println("Invalid status. Please select 1-4.");
            }
        }
    }

    private static int readProgress() {
        while (true) {
            int progress = readInt("Enter Progress (0-100): ");

            if (progress >= 0 && progress <= 100) {
                return progress;
            }

            System.out.println("Progress must be between 0 and 100.");
        }
    }

    private static BigDecimal readBigDecimal(String message) {
        while (true) {
            String value = readRequiredString(message);

            try {
                BigDecimal amount = new BigDecimal(value);

                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Cost cannot be negative.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static LocalDate readDate(String message) {
        while (true) {
            String value = readRequiredString(message);

            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
            }
        }
    }

    private static String readRequiredString(String message) {
        while (true) {
            System.out.print(message);

            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static boolean isAdminOrManager(User user) {
        String role = user.getRoleName();

        return "ADMIN".equalsIgnoreCase(role)
                || "MANAGER".equalsIgnoreCase(role)
                || "PROJECT_MANAGER".equalsIgnoreCase(role);
    }

    private static boolean isTeamLead(User user) {
        return "TEAM_LEAD".equalsIgnoreCase(user.getRoleName());
    }

    private static String safeMessage(Exception e) {
        if (e.getMessage() == null || e.getMessage().isBlank()) {
            return e.getClass().getSimpleName();
        }

        return e.getMessage();
    }
}
