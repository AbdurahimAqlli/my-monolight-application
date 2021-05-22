package uz.asz.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.asz.myapp.IntegrationTest;
import uz.asz.myapp.domain.Group;
import uz.asz.myapp.domain.enumeration.Category;
import uz.asz.myapp.domain.enumeration.GroupStatus;
import uz.asz.myapp.repository.GroupRepository;

/**
 * Integration tests for the {@link GroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Category DEFAULT_CATEGORY = Category.B;
    private static final Category UPDATED_CATEGORY = Category.BC;

    private static final Instant DEFAULT_BEGIN_DRIVING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGIN_DRIVING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TELEGRAM_GROUP_LINK = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_GROUP_LINK = "BBBBBBBBBB";

    private static final GroupStatus DEFAULT_STATUS = GroupStatus.BEGIN_LESSONS;
    private static final GroupStatus UPDATED_STATUS = GroupStatus.END_LESSONS;

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group()
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .beginDrivingDate(DEFAULT_BEGIN_DRIVING_DATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .telegramGroupLink(DEFAULT_TELEGRAM_GROUP_LINK)
            .status(DEFAULT_STATUS);
        return group;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity(EntityManager em) {
        Group group = new Group()
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .beginDrivingDate(UPDATED_BEGIN_DRIVING_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .telegramGroupLink(UPDATED_TELEGRAM_GROUP_LINK)
            .status(UPDATED_STATUS);
        return group;
    }

    @BeforeEach
    public void initTest() {
        group = createEntity(em);
    }

    @Test
    @Transactional
    void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();
        // Create the Group
        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGroup.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testGroup.getBeginDrivingDate()).isEqualTo(DEFAULT_BEGIN_DRIVING_DATE);
        assertThat(testGroup.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGroup.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testGroup.getTelegramGroupLink()).isEqualTo(DEFAULT_TELEGRAM_GROUP_LINK);
        assertThat(testGroup.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createGroupWithExistingId() throws Exception {
        // Create the Group with an existing ID
        group.setId(1L);

        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].beginDrivingDate").value(hasItem(DEFAULT_BEGIN_DRIVING_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].telegramGroupLink").value(hasItem(DEFAULT_TELEGRAM_GROUP_LINK)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.beginDrivingDate").value(DEFAULT_BEGIN_DRIVING_DATE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.telegramGroupLink").value(DEFAULT_TELEGRAM_GROUP_LINK))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .beginDrivingDate(UPDATED_BEGIN_DRIVING_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .telegramGroupLink(UPDATED_TELEGRAM_GROUP_LINK)
            .status(UPDATED_STATUS);

        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testGroup.getBeginDrivingDate()).isEqualTo(UPDATED_BEGIN_DRIVING_DATE);
        assertThat(testGroup.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGroup.getTelegramGroupLink()).isEqualTo(UPDATED_TELEGRAM_GROUP_LINK);
        assertThat(testGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, group.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .beginDrivingDate(UPDATED_BEGIN_DRIVING_DATE)
            .endDate(UPDATED_END_DATE)
            .telegramGroupLink(UPDATED_TELEGRAM_GROUP_LINK)
            .status(UPDATED_STATUS);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testGroup.getBeginDrivingDate()).isEqualTo(UPDATED_BEGIN_DRIVING_DATE);
        assertThat(testGroup.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGroup.getTelegramGroupLink()).isEqualTo(UPDATED_TELEGRAM_GROUP_LINK);
        assertThat(testGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .beginDrivingDate(UPDATED_BEGIN_DRIVING_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .telegramGroupLink(UPDATED_TELEGRAM_GROUP_LINK)
            .status(UPDATED_STATUS);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testGroup.getBeginDrivingDate()).isEqualTo(UPDATED_BEGIN_DRIVING_DATE);
        assertThat(testGroup.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGroup.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGroup.getTelegramGroupLink()).isEqualTo(UPDATED_TELEGRAM_GROUP_LINK);
        assertThat(testGroup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, group.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Delete the group
        restGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, group.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
